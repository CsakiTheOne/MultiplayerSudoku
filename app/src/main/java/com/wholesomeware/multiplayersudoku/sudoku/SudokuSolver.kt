package com.wholesomeware.multiplayersudoku.sudoku

class SudokuSolver {
    companion object {

        private const val GRID_SIZE = 9
        private const val GRID_SIZE_SQUARE_ROOT = 3
        private const val BOX_SIZE = 3
        private const val MIN_DIGIT_VALUE = 1
        private const val MAX_DIGIT_VALUE = 9
        private const val MIN_DIGIT_INDEX = 0
        private const val MAX_DIGIT_INDEX = 8

        private var grid = Array(GRID_SIZE) { IntArray(GRID_SIZE) }

        fun Sudoku.isDone(): Boolean {
            return isGridCorrect(currentGrid) && currentGrid.all { row -> row.all { it != 0 } }
        }

        fun isGridSolvable(grid: Array<IntArray>): Boolean {
            this.grid = grid.copy()
            return solve()
        }

        fun isGridCorrect(grid: Array<IntArray>): Boolean {
            if (grid.isEmpty() || grid.first().isEmpty()) return false

            val rows: List<List<Int>> = grid.map { row -> row.filter { it != 0 } }
            if (rows.any { it.size != it.toSet().size }) {
                return false
            }
            val columns: List<List<Int>> =
                (0 until GRID_SIZE).map { column -> grid.map { it[column] }.filter { it != 0 } }
            if (columns.any { it.size != it.toSet().size }) {
                return false
            }
            val boxes: List<List<Int>> =
                (0 until GRID_SIZE step GRID_SIZE_SQUARE_ROOT).flatMap { rowStart ->
                    (0 until GRID_SIZE step GRID_SIZE_SQUARE_ROOT).map { columnStart ->
                        (rowStart until rowStart + GRID_SIZE_SQUARE_ROOT).flatMap { row ->
                            (columnStart until columnStart + GRID_SIZE_SQUARE_ROOT).map { column -> grid[row][column] }
                        }.filter { it != 0 }
                    }
                }
            return !boxes.any { it.size != it.toSet().size }
        }

        private fun Array<IntArray>.copy() = Array(size) { get(it).clone() }

        private fun solve(): Boolean {
            for (i in 0 until GRID_SIZE) {
                for (j in 0 until GRID_SIZE) {
                    if (grid[i][j] == 0) {
                        val availableDigits = getAvailableDigits(i, j)
                        for (k in availableDigits) {
                            grid[i][j] = k
                            if (solve()) {
                                return true
                            }
                            grid[i][j] = 0
                        }
                        return false
                    }
                }
            }
            return true
        }

        private fun getAvailableDigits(row: Int, column: Int): Iterable<Int> {
            val digitsRange = MIN_DIGIT_VALUE..MAX_DIGIT_VALUE
            var availableDigits = mutableSetOf<Int>()
            availableDigits.addAll(digitsRange)

            truncateByDigitsAlreadyUsedInRow(availableDigits, row)
            if (availableDigits.size > 1) {
                truncateByDigitsAlreadyUsedInColumn(availableDigits, column)
            }
            if (availableDigits.size > 1) {
                truncateByDigitsAlreadyUsedInBox(availableDigits, row, column)
            }

            return availableDigits.asIterable()
        }

        private fun truncateByDigitsAlreadyUsedInRow(availableDigits: MutableSet<Int>, row: Int) {
            for (i in MIN_DIGIT_INDEX..MAX_DIGIT_INDEX) {
                if (grid[row][i] != 0) {
                    availableDigits.remove(grid[row][i])
                }
            }
        }

        private fun truncateByDigitsAlreadyUsedInColumn(
            availableDigits: MutableSet<Int>,
            column: Int
        ) {
            for (i in MIN_DIGIT_INDEX..MAX_DIGIT_INDEX) {
                if (grid[i][column] != 0) {
                    availableDigits.remove(grid[i][column])
                }
            }
        }

        private fun truncateByDigitsAlreadyUsedInBox(
            availableDigits: MutableSet<Int>,
            row: Int,
            column: Int
        ) {
            val rowStart = findBoxStart(row)
            val rowEnd = findBoxEnd(rowStart)
            val columnStart = findBoxStart(column)
            val columnEnd = findBoxEnd(columnStart)

            for (i in rowStart until rowEnd) {
                for (j in columnStart until columnEnd) {
                    if (grid[i][j] != 0) {
                        availableDigits.remove(grid[i][j])
                    }
                }
            }
        }

        private fun findBoxStart(index: Int) = index - index % GRID_SIZE_SQUARE_ROOT

        private fun findBoxEnd(index: Int) = index + BOX_SIZE - 1

    }
}