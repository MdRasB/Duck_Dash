# 🦆 Duck Dash

![Java Version](https://img.shields.io/badge/Java-17%2B-orange.svg)
![JavaFX Version](https://img.shields.io/badge/JavaFX-21-blue.svg)
![Build](https://img.shields.io/badge/Build-Maven-red.svg)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)

**Duck Dash** is a high-octane, 2D side-scrolling runner built with JavaFX. Players control a determined duck navigating through a challenging environment filled with obstacles, enemies, and power-ups. Manage your health and sleep levels while dashing through levels to achieve the highest score!

---

## 🎮 Features

*   **Dynamic Game Loop:** Smooth 60FPS gameplay powered by JavaFX AnimationTimer with delta-time calculation for frame-rate independent movement.
*   **Complex Entity System:** Includes a variety of obstacles (Chairs, Bottles), enemies (Cats, Eagles, Worms), and collectibles (Bread).
*   **Resource Management:** Custom AssetLoader with an intelligent caching system and preloading to ensure zero-lag gameplay.
*   **Interactive HUD:** Real-time tracking of Health and Sleep bars, along with a persistent game timer.
*   **State-of-the-art UI:** Polished menus including Story Mode, Level Selection, Settings, and a Pause system.
*   **Responsive Controls:** Precision jumping and crouching mechanics to dodge multi-level threats.

---

## 🛠 Tech Stack

*   **Language:** Java 17
*   **Framework:** JavaFX 21
*   **Build Tool:** Maven
*   **Media Handling:** JavaFX Media API for SFX and Music

---

## 🚀 Getting Started

### Prerequisites

*   JDK 17 or higher installed.
*   Maven 3.8+ installed.
*   (Optional) An IDE like IntelliJ IDEA or VS Code.

### Installation & Running

1.  **Clone the Repository:**
    ```bash
    git clone https://github.com/MdRasB/2D_Duck_in_Bauet.git
    cd DuckDash
    ```

2.  **Build the Project:**
    ```bash
    mvn clean install
    ```

3.  **Launch the Game:**
    ```bash
    mvn javafx:run
    ```

---

## 🕹 Controls

| Action | Key / Input |
| :--- | :--- |
| **Jump** | `Space` / `Up Arrow` / `W` |
| **Crouch** | `Down Arrow` / `S` |
| **Pause** | `P` / `Esc` / On-screen Button |
| **Menu Navigation** | Mouse Click |

---

## 📂 Project Structure

```text
src/main/java/edu/bauet/java/cse/duckrun/
├── core/         # Game engine, loop logic, and global constants.
├── entities/     # Player, Enemy, Obstacle, and Item classes.
├── levels/       # Level definitions and spawning logic.
├── scenes/       # Screen management (Menu, Game, Story, Game Over).
├── ui/           # Custom UI components (HealthBar, Menus).
└── utils/        # Asset loading, collision detection, and timers.
```

---

## 🧭 Interactive Flowchart

The full project flow is documented in `PROJECT_FLOWCHART.md`, and can be viewed interactively (pan/zoom + export) here:

- `docs/duckrun_flowchart_v2.html` (generated from `PROJECT_FLOWCHART.md`)
- Regenerate anytime: `scripts/generate_flowchart_viewer.sh`
- See the flowchart : [Flowchart](https://rawcdn.githack.com/MdRasB/2D_Duck_in_Bauet/refs/heads/r_flow/docs/duckrun_flowchart_v2.html)


---

## 🖼️ Screenshots

### Main Menu
![Main Menu](src/main/resources/images/ui/main_menu.png)

### In-Game Action
![Gameplay](src/main/resources/images/ui/game_play_1.png)

---

## 🤝 Contributing

Contributions are what make the open-source community such an amazing place to learn, inspire, and create.

1.  Fork the Project.
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`).
4.  Push to the Branch (`git push origin feature/AmazingFeature`).
5.  Open a Pull Request.

---

## 📜 License

Distributed under the MIT License. See `LICENSE` for more information.

---

## ✉️ Contact

Project Link: [https://github.com/MdRasB/2D_Duck_in_Bauet](https://github.com/MdRasB/2D_Duck_in_Bauet)

Developed with ❤️ by the **Duck Dash Team**.
