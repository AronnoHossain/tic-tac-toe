package com.pinwheel.tictactoe

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlin.math.min

// --- Models ---
private enum class CellState { EMPTY, X, O }
private enum class ScreenState { MODE, DIFFICULTY, GAME, RESULT }
private enum class Difficulty { EASY, MEDIUM, HARD }
private enum class PlayerMode { SINGLE, MULTI }
private enum class GameResult { X_WIN, O_WIN, DRAW }

@Composable
fun TicTacToeMainActivityContent() {
    var screen by remember { mutableStateOf(ScreenState.MODE) }
    var playerMode by remember { mutableStateOf(PlayerMode.SINGLE) }
    var difficulty by remember { mutableStateOf(Difficulty.MEDIUM) }
    var gameResult by remember { mutableStateOf<GameResult?>(null) }
    var finalBoard by remember { mutableStateOf(List(9) { CellState.EMPTY }) }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            when (screen) {
                ScreenState.MODE -> ModeScreen(
                    onSingle = { playerMode = PlayerMode.SINGLE; screen = ScreenState.DIFFICULTY },
                    onMulti = { playerMode = PlayerMode.MULTI; screen = ScreenState.GAME }
                )
                ScreenState.DIFFICULTY -> DifficultyScreen(
                    onPick = { difficulty = it; screen = ScreenState.GAME },
                    onBack = { screen = ScreenState.MODE }
                )
                ScreenState.GAME -> GameScreenHost(
                    playerMode = playerMode,
                    difficulty = difficulty,
                    onBackToMode = { screen = ScreenState.MODE },
                    onGameResult = { result, board ->
                        gameResult = result
                        finalBoard = board
                        screen = ScreenState.RESULT
                    }
                )
                ScreenState.RESULT -> ResultScreen(
                    result = gameResult ?: GameResult.DRAW,
                    onStartOver = {
                        screen = ScreenState.MODE
                        gameResult = null
                        finalBoard = List(9) { CellState.EMPTY }
                    }
                )
            }
        }
    }
}

@Composable
private fun ModeScreen(onSingle: () -> Unit, onMulti: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Tic Tac Toe", style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.height(20.dp))
            IconTextButton(
                imageVector = null,
                buttonText = "Single Player",
                onClick = onSingle
            )
            Spacer(Modifier.height(12.dp))
            IconTextButton(
                imageVector = null,
                buttonText = "Multiplayer",
                onClick = onMulti
            )
        }
    }
}

@Composable
private fun DifficultyScreen(onPick: (Difficulty) -> Unit, onBack: () -> Unit) {
    BackHandler { onBack() }
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Choose Difficulty", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
            Spacer(Modifier.height(16.dp))
            IconTextButton(
                imageVector = null,
                buttonText = "Easy",
                onClick = { onPick(Difficulty.EASY) }
            )
            Spacer(Modifier.height(8.dp))
            IconTextButton(
                imageVector = null,
                buttonText = "Medium",
                onClick = { onPick(Difficulty.MEDIUM) }
            )
            Spacer(Modifier.height(8.dp))
            IconTextButton(
                imageVector = null,
                buttonText = "Hard",
                onClick = { onPick(Difficulty.HARD) }
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@Composable
private fun ResultScreen(
    result: GameResult,
    onStartOver: () -> Unit
) {
    BackHandler { onStartOver() }
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            // Result message
            Text(
                text = when (result) {
                    GameResult.X_WIN -> "X Wins!"
                    GameResult.O_WIN -> "O Wins!"
                    GameResult.DRAW -> "It's a Draw!"
                },
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(Modifier.height(32.dp))

            // Start Over button
            IconTextButton(
                imageVector = null,
                buttonText = "Start Over",
                onClick = onStartOver
            )
        }
    }
}

@Composable
private fun GameScreenHost(playerMode: PlayerMode, difficulty: Difficulty, onBackToMode: () -> Unit, onGameResult: (GameResult, List<CellState>) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Box(modifier = Modifier.fillMaxSize()) {
            ResponsiveTicTacToe(
                isSinglePlayer = (playerMode == PlayerMode.SINGLE),
                difficulty = difficulty,
                onExitToMode = onBackToMode,
                onGameResult = onGameResult
            )
        }
    }
}

/* ----------------------
   Main game composable
   ---------------------- */
@Composable
private fun ResponsiveTicTacToe(
    isSinglePlayer: Boolean,
    difficulty: Difficulty,
    onExitToMode: () -> Unit,
    onGameResult: (GameResult, List<CellState>) -> Unit
) {
    // game state
    var board by remember { mutableStateOf(List(9) { CellState.EMPTY }) }
    var xTurn by remember { mutableStateOf(true) } // X starts
    var winningLine by remember { mutableStateOf<IntArray?>(null) }

    // animation / AI state
    val scope = rememberCoroutineScope()
    val cellAnim = remember { List(9) { Animatable(0f) } }         // animate each cell on fill
    val winAnim = remember { Animatable(0f) }                     // winning line animation
    var isAiThinking by remember { mutableStateOf(false) }         // AI busy state (single-player only)
    var aiFocusIndex by remember { mutableStateOf<Int?>(null) }    // current focused cell scanned
    val pulse = rememberInfiniteTransition()
    val pulseProgress by pulse.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(900, easing = FastOutSlowInEasing), repeatMode = RepeatMode.Reverse)
    )

    BackHandler { onExitToMode() }

    // detect winner or draw and trigger win animation
    LaunchedEffect(board) {
        // 1) Animate all newly filled cells
        val jobs = board.mapIndexedNotNull { i, s ->
            if (s != CellState.EMPTY && cellAnim[i].value == 0f) {
                scope.launch {
                    cellAnim[i].animateTo(1f, tween(360, easing = FastOutSlowInEasing))
                }
            } else null
        }

        // Wait for all cell animations to complete (important!)
        if (jobs.isNotEmpty()) jobs.joinAll()

        // 2) Check winner or draw
        val win = findWinningLine(board)
        if (win != null) {
            winningLine = win
            // Animate winning line
            winAnim.snapTo(0f)
            winAnim.animateTo(1f, tween(600, easing = FastOutSlowInEasing))

            // Keep winning line visible before showing result
            delay(1200)

            // Determine winner and show result screen
            val winner = board[win[0]]
            val result = when (winner) {
                CellState.X -> GameResult.X_WIN
                CellState.O -> GameResult.O_WIN
                else -> GameResult.DRAW
            }
            onGameResult(result, board)
        } else if (board.none { it == CellState.EMPTY }) {
            // draw -> show result screen
            delay(1000)
            onGameResult(GameResult.DRAW, board)
        }
    }

    // AI behavior (single-player): scanning + thinking + move
    LaunchedEffect(xTurn, isSinglePlayer, board, winningLine) {
        if (isSinglePlayer && !xTurn && winningLine == null) {
            isAiThinking = true
            // scanning highlight
            val scanJob = scope.launch {
                val empties = board.withIndex().filter { it.value == CellState.EMPTY }.map { it.index }
                if (empties.isEmpty()) return@launch
                repeat(6) {
                    aiFocusIndex = empties.random()
                    delay((220L..420L).random())
                }
                aiFocusIndex = null
            }

            // thinking delay based on difficulty
            val thinkingDelay = when (difficulty) {
                Difficulty.EASY -> (300L..700L).random()
                Difficulty.MEDIUM -> (700L..1200L).random()
                Difficulty.HARD -> (1000L..1700L).random()
            }
            delay(thinkingDelay)

            val move = computeAIMove(board, difficulty)
            if (move != -1) {
                board = board.toMutableList().apply { this[move] = CellState.O }
                xTurn = true
            }
            scanJob.cancel()
            aiFocusIndex = null
            isAiThinking = false
        }
    }

    // turn label
    val turnLabel = when {
        winningLine != null -> "Game Over"
        isSinglePlayer && isAiThinking -> "AI is thinking"
        xTurn -> "X's turn"
        else -> "O's turn"
    }

    // UI shell: 20.dp padding around board
    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).padding(20.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize()) {
            // turn indicator
            Text(turnLabel, color = MaterialTheme.colorScheme.onBackground, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(20.dp))

            // Board area: square aspect ratio, responsive
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                val onBackgroundColor = MaterialTheme.colorScheme.onBackground
                val primaryColor = MaterialTheme.colorScheme.primary
                val errorColor = MaterialTheme.colorScheme.error
                val secondaryColor = MaterialTheme.colorScheme.secondary
                Canvas(modifier = Modifier.fillMaxSize().aspectRatio(1f)
                    .pointerInput(board, xTurn, winningLine, isAiThinking, aiFocusIndex) {
                        detectTapGestures { tap ->
                            val w = size.width.toFloat()
                            val h = size.height.toFloat()
                            val boardPx = min(w, h)
                            val cellSize = boardPx / 3f
                            val offsetX = (w - boardPx) / 2f
                            val offsetY = (h - boardPx) / 2f

                            // if someone won or draw, tapping does nothing (auto-reset handled)
                            if (winningLine != null) return@detectTapGestures
                            // ignore taps during AI turn
                            if (isSinglePlayer && !xTurn) return@detectTapGestures

                            val localX = tap.x - offsetX
                            val localY = tap.y - offsetY
                            if (localX < 0f || localY < 0f || localX > boardPx || localY > boardPx) return@detectTapGestures

                            val col = (localX / cellSize).toInt().coerceIn(0, 2)
                            val row = (localY / cellSize).toInt().coerceIn(0, 2)
                            val idx = row * 3 + col

                            if (board[idx] == CellState.EMPTY) {
                                board = board.toMutableList().apply { this[idx] = if (xTurn) CellState.X else CellState.O }
                                xTurn = !xTurn
                            }
                        }
                    }
                ) {
                    // Canvas DrawScope
                    val w = size.width
                    val h = size.height
                    val boardPx = min(w, h)
                    val cellSize = boardPx / 3f
                    val startX = (w - boardPx) / 2f
                    val startY = (h - boardPx) / 2f
                    val stroke = boardPx * 0.02f
                    val margin = boardPx * 0.06f

                    // grid lines (theme color)
                    val gridColor = onBackgroundColor
                    for (i in 1..2) {
                        drawLine(gridColor, Offset(startX + i * cellSize, startY), Offset(startX + i * cellSize, startY + boardPx), strokeWidth = stroke)
                        drawLine(gridColor, Offset(startX, startY + i * cellSize), Offset(startX + boardPx, startY + i * cellSize), strokeWidth = stroke)
                    }

                    // AI focus glow behind candidate cell
                    aiFocusIndex?.let { idx ->
                        val col = idx % 3
                        val row = idx / 3
                        val cx = startX + col * cellSize + cellSize / 2f
                        val cy = startY + row * cellSize + cellSize / 2f
                        val maxR = cellSize * 0.45f
                        val r = maxR * (0.6f + 0.4f * pulseProgress)
                        val alpha = 0.12f + 0.25f * pulseProgress
                        drawCircle(primaryColor.copy(alpha = alpha), radius = r, center = Offset(cx, cy))
                    }

                    // draw X and O based on board; pass animation progress values
                    for (i in board.indices) {
                        val col = i % 3
                        val row = i / 3
                        val x = startX + col * cellSize
                        val y = startY + row * cellSize
                        val p = cellAnim[i].value
                        when (board[i]) {
                            CellState.X -> drawAnimatedX(x, y, cellSize, margin, stroke, p, errorColor)
                            CellState.O -> drawAnimatedO(x, y, cellSize, stroke, p, primaryColor)
                            else -> {}
                        }
                    }

                    // winning line drawn with winAnim.value
                    winningLine?.let { line ->
                        val start = cellCenter(line[0], startX, startY, cellSize)
                        val end = cellCenter(line[2], startX, startY, cellSize)
                        val p = winAnim.value
                        val current = Offset(start.x + (end.x - start.x) * p, start.y + (end.y - start.y) * p)
                        drawLine(secondaryColor, start, current, strokeWidth = stroke * 1.25f)
                    }
                }
            }
        }
    }
}

/* ----------------------
   Draw helpers (pure DrawScope)
   ---------------------- */
private fun DrawScope.drawAnimatedX(x: Float, y: Float, cellSize: Float, margin: Float, stroke: Float, progress: Float, color: androidx.compose.ui.graphics.Color) {
    val len = (cellSize - 2f * margin) * progress
    val sx = x + margin
    val sy = y + margin
    drawLine(color, Offset(sx, sy), Offset(sx + len, sy + len), strokeWidth = stroke)
    drawLine(color, Offset(sx, y + cellSize - margin), Offset(sx + len, y + cellSize - margin - len), strokeWidth = stroke)
}

private fun DrawScope.drawAnimatedO(x: Float, y: Float, cellSize: Float, stroke: Float, progress: Float, color: androidx.compose.ui.graphics.Color) {
    // draw arc that sweeps from -90 to -90 + 360 * progress
    val inset = cellSize * 0.12f
    val size = Size(cellSize - inset * 2f, cellSize - inset * 2f)
    drawArc(color = color, startAngle = -90f, sweepAngle = 360f * progress, useCenter = false, topLeft = Offset(x + inset, y + inset), size = size, style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke))
}

private fun cellCenter(index: Int, startX: Float, startY: Float, cellSize: Float): Offset {
    val col = index % 3
    val row = index / 3
    return Offset(startX + col * cellSize + cellSize / 2f, startY + row * cellSize + cellSize / 2f)
}

/* ----------------------
   Game logic & AI
   ---------------------- */
private fun findWinningLine(board: List<CellState>): IntArray? {
    val lines = arrayOf(
        intArrayOf(0,1,2), intArrayOf(3,4,5), intArrayOf(6,7,8),
        intArrayOf(0,3,6), intArrayOf(1,4,7), intArrayOf(2,5,8),
        intArrayOf(0,4,8), intArrayOf(2,4,6)
    )
    for (l in lines) {
        val a = l[0]; val b = l[1]; val c = l[2]
        if (board[a] != CellState.EMPTY && board[a] == board[b] && board[a] == board[c]) return l
    }
    return null
}

private fun computeAIMove(board: List<CellState>, difficulty: Difficulty): Int {
    val empties = board.mapIndexedNotNull { i, v -> if (v == CellState.EMPTY) i else null }
    if (empties.isEmpty()) return -1
    return when (difficulty) {
        Difficulty.EASY -> empties.random()
        Difficulty.MEDIUM -> {
            // try winning, then blocking, else random
            findWinningIndex(board, CellState.O) ?: findWinningIndex(board, CellState.X) ?: empties.random()
        }
        Difficulty.HARD -> {
            // minimax with full search (small tree)
            val (idx, _) = minimax(board, CellState.O)
            if (idx == -1) empties.random() else idx
        }
    }
}

private fun findWinningIndex(board: List<CellState>, symbol: CellState): Int? {
    val lines = arrayOf(
        intArrayOf(0,1,2), intArrayOf(3,4,5), intArrayOf(6,7,8),
        intArrayOf(0,3,6), intArrayOf(1,4,7), intArrayOf(2,5,8),
        intArrayOf(0,4,8), intArrayOf(2,4,6)
    )
    for (l in lines) {
        val a = l[0]; val b = l[1]; val c = l[2]
        val vals = listOf(board[a], board[b], board[c])
        if (vals.count { it == symbol } == 2 && vals.contains(CellState.EMPTY)) {
            val emptyIdxInLine = vals.indexOf(CellState.EMPTY)
            return l[emptyIdxInLine]
        }
    }
    return null
}

/**
 * Minimax: returns Pair(index, score). Score: +1 good for X, -1 good for O, 0 draw.
 * We evaluate terminal states and recurse. Tic-tac-toe tree is small so full search is OK.
 */
private fun minimax(board: List<CellState>, player: CellState): Pair<Int, Int> {
    val win = findWinningLine(board)
    if (win != null) {
        val p = board[win[0]]
        return -1 to if (p == CellState.X) 1 else -1
    }
    if (board.none { it == CellState.EMPTY }) return -1 to 0

    val empties = board.withIndex().filter { it.value == CellState.EMPTY }.map { it.index }
    val moves = mutableListOf<Pair<Int, Int>>()
    for (idx in empties) {
        val nb = board.toMutableList()
        nb[idx] = player
        val (_, score) = minimax(nb, if (player == CellState.X) CellState.O else CellState.X)
        moves.add(idx to score)
    }
    return if (player == CellState.X) {
        moves.maxByOrNull { it.second } ?: (-1 to 0)
    } else {
        moves.minByOrNull { it.second } ?: (-1 to 0)
    }
}