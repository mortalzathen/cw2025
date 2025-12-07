# 🧊 TetrisJFX: A Modular JavaFX-Based Tetris Clone

This project is a modern, feature-rich implementation of the classic Tetris game built using **JavaFX** and structured with **Maven**. It utilizes the Model-View-Controller (MVC) architectural pattern to ensure clean separation of concerns, maintainability, and testability.

---

## 🚀 1. Quick Start: Running the Application

This project uses the **Maven Wrapper** (`mvnw` or `mvnw.cmd`) to ensure a consistent build environment, regardless of the Maven version installed globally on your system.

### Prerequisites

1.  **Java Development Kit (JDK) 23:** Must be installed on your system.
2.  **JAVA\_HOME Environment Variable:** Must be set and pointing to your JDK installation path (e.g., `C:\Program Files\Java\jdk-23`).

### Execution Steps
#### 🛠️ Windows (Bash Terminals only: Git Bash / MINGW64)

If you are using a Bash terminal on Windows, you **must run these two commands first** in your current session to correctly set the path variables using Bash syntax:

```bash
# 1. Set the JAVA_HOME variable using the Bash path format (forward slashes)
export JAVA_HOME="/C/Program Files/Java/jdk-23"

# 2. Add the JDK's bin directory to the system PATH variable
export PATH="$JAVA_HOME/bin:$PATH"
```
Navigate to the project's **root directory** in your terminal (where `pom.xml`, `mvnw`, and `mvnw.cmd` are located).

| Operating System | Build Command | Run Command | Notes |
| :--- | :--- | :--- | :--- |
| **Linux / macOS (Bash)** | `./mvnw clean package` | `./mvnw javafx:run` | Ensure execution permission (`chmod +x mvnw`) |
| **Windows (CMD/PowerShell)**| `.\mvnw.cmd clean package` | `.\mvnw.cmd javafx:run` | Use the `.cmd` extension |
| **Windows (Git Bash/WSL)** | `./mvnw clean package` | `./mvnw javafx:run` | Use the plain `./mvnw` script |

---

## 🎮 2. Gameplay and Controls

The game features standard Tetris mechanics, including soft drop, hard drop, and rotational wall kicks (simplified).

| Action | Key Bindings |
| :--- | :--- |
| **Move Left** | $\leftarrow$ / **A** |
| **Move Right** | $\rightarrow$ / **D** |
| **Soft Drop** | $\downarrow$ / **S** |
| **Rotate (Left)** | $\uparrow$ / **W** |
| **Hard Drop** | **SPACE** |
| **Toggle Shadow** | **H** |
| **Pause/Resume** | **BACKSPACE** |
| **New Game** | **N** |

---

## 📐 3. Architectural Report: Evolution to MVC

The project was refactored from an initial flat file structure to enforce a strict **Model-View-Controller (MVC)** pattern. This separation of concerns is critical for the stability and maintenance of the codebase.

### A. Architectural Restructuring

| Old Structure | New Package | Core Responsibility |
| :--- | :--- | :--- |
| `com.comp2042` (Flat) | **`engine`** | **Controller/Engine:** Manages the game loop, state transitions, and coordination of actions. |
| `com.comp2042` (Flat) | **`ui`** | **View/View Controller:** Handles all rendering, scene transitions, and translation of user input (keys) into events. |
| `logic.bricks` | **`models`** | **Model/Data:** Defines the game's state (Score, Board Matrix), data structures (Bricks, Scores), and persistence logic. |
| N/A | **`events`** | **Communication:** Provides Data Transfer Objects (DTOs) and event interfaces to decouple the layers. |
| N/A | **`util`** | **Utilities:** Contains core helper classes and configurations (`MatrixOperations`, `Constants`). |

### B. Explanation of Key Class Roles and Changes

| Class | Package | Primary Role and Function in the Final Code | Changes/Refinements from Base Code |
| :--- | :--- | :--- | :--- |
| **`Constants.java`** | `com.comp2042` | **Global Configuration.** Defines all immutable settings, including **Board Dimensions** (`10x25`), **Spawn Coordinates** (`START_X`, `START_Y`), **Tile Size**, and all **Brick IDs** (1 through 7). | **New Implementation:** Created to eliminate all "magic numbers," centralizing configuration for easy maintenance and clarity. |
| **`SimpleBoard.java`** | `engine` | **Game State Manager.** Implements the `Board` interface. Responsible for the physics of the falling piece, collision checks, merging, and game over detection. | **Major Extension:** Added logic for **Ghost Piece calculation** (`getGhostYPosition`) and **Hard Drop** (`hardDropBrick`). Logic for movement (`moveBrickDown`, `rotateLeftBrick`) strictly follows the *Check-Then-Move* pattern for collision safety. |
| **`GameController.java`** | `engine` | **Central Game Loop Coordinator.** Implements `InputEventListener`. Coordinates commands between the view (`GuiController`) and the state (`SimpleBoard`). | **Major Extension:** Implements the core game loop structure: **Move $\rightarrow$ Merge on Collision $\rightarrow$ Clear Rows $\rightarrow$ Check Game Over.** Also handles triggering the score notification and submitting the score to the `LeaderboardManager`. |
| **`MatrixOperations.java`** | `util` | **Utility Engine for Matrix Manipulation.** Provides static methods for array copying, merging, collision checking, and row clearing. | **Refinement:** The `checkRemoving` method was implemented to calculate the **non-linear score bonus** ($50 \times \text{rows}^2$), rewarding large clears exponentially. |
| **`BrickRotator.java`** | `util` | **Rotation State Manager.** Holds the current piece and determines the next rotational shape index. | **New Implementation:** Dedicated class for rotation logic. It ensures rotation indices **wrap around** back to 0 using the **modulo operator (`%`)** when reaching the end of the shape list. |
| **`RandomBrickGenerator.java`** | `models.bricks` | **Piece Supplier.** Creates and queues the sequence of Tetrominoes. | **Major Extension:** Updated to use a **queue/bag system** (`ArrayDeque`) and added the `getNextBricks(int count)` method to support the new **three-piece lookahead** feature required by the GUI. |
| **`GuiController.java`** | `ui` | **The View Manager.** Handles all JavaFX rendering, the game clock (`Timeline`), and input translation. | **Major Extension:** Implements rendering logic for the **current brick, the ghost piece**, and the **three next-piece previews** using data provided via the `ViewData` DTO. |
| **`LeaderboardManager.java`** | `models` | **High Score Persistence.** Manages saving and loading of the top scores. | **New Implementation:** Uses **Java Object Serialization** to handle persistence to a file (`leaderboard.dat`), and strictly maintains a sorted list of the top 10 scores. |
| **`Score.java`** | `models` | **Observable Score Tracker.** | **Refinement:** Utilizes a JavaFX `SimpleIntegerProperty` to enable the score label in the GUI to be automatically **data-bound** to the score value, eliminating manual refresh calls. |
| **`ViewData.java`** | `events` | **View Model DTO.** Transfers necessary rendering data. | **Extension:** Updated to include fields for the **Ghost Y Position** and **three next-piece data matrices**, ensuring the GUI always has the complete snapshot of the game state. |

---

## 🎨 4. Aesthetic and UI Features

The entire application uses a dark, high-contrast, digital theme, achieved through coordinated CSS and resource files.

* **Custom Font:** The **`digital.ttf`** font is loaded and used extensively via CSS for scores, menus, and the Game Over screen to achieve a retro-digital aesthetic.
* **Background:** The root container uses a tiled **`background_image.png`** for a subtle, repeating grid pattern, reinforcing the game's nature.
* **Animated Notifications:** The **`NotificationPanel`** implements a smooth **`ParallelTransition`** (fading out while moving up) to display score bonuses, providing clean visual feedback on line clears.
* **Game Board:** Features a recessed black play area with a thick, stylized border and individual tiles sporting a slight `dropshadow` effect to give the pieces a 'pop' of depth.