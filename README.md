🦆 Duck Dash


![Java Version (https://img.shields.io/badge/Java-17%2B-orange.svg)](https://www.oracle.com/java/)
![JavaFX Version (https://img.shields.io/badge/JavaFX-21-blue.svg)](https://openjfx.io/)
![Build (https://img.shields.io/badge/Build-Maven-red.svg)](https://maven.apache.org/)
![License: MIT (https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


Duck Dash is a high-octane, 2D side-scrolling runner built with JavaFX. Players control a determined duck navigating through a challenging environment filled with
obstacles, enemies, and power-ups. Manage your health and sleep levels while dashing through levels to achieve the highest score!

  ---

🎮 Features


* Dynamic Game Loop: Smooth 60FPS gameplay powered by JavaFX AnimationTimer with delta-time calculation for frame-rate independent movement.
* Complex Entity System: Includes a variety of obstacles (Chairs, Bottles), enemies (Cats, Eagles, Worms), and collectibles (Bread).
* Resource Management: Custom AssetLoader with an intelligent caching system and preloading to ensure zero-lag gameplay.
* Interactive HUD: Real-time tracking of Health and Sleep bars, along with a persistent game timer.
* State-of-the-art UI: Polished menus including Story Mode, Level Selection, Settings, and a Pause system.
* Responsive Controls: Precision jumping and crouching mechanics to dodge multi-level threats.

  ---

🛠 Tech Stack


- Language: Java 17
- Framework: JavaFX 21
- Build Tool: Maven
- Media Handling: JavaFX Media API for SFX and Music

  ---

🚀 Getting Started

Prerequisites


* JDK 17 or higher installed.
* Maven 3.8+ installed.
* (Optional) An IDE like IntelliJ IDEA or VS Code.

Installation & Running

1. Clone the Repository:
   1     git clone https://github.com/your-username/DuckDash.git
   2     cd DuckDash

2. Build the Project:


1     mvn clean install

3. Launch the Game:
   1     mvn javafx:run

  ---

🕹 Controls



┌─────────────────┬────────────────────────────┐
│ Action          │ Key / Input                │
├─────────────────┼────────────────────────────┤
│ Jump            │ Space / Up Arrow / W       │
│ Crouch          │ Down Arrow / S             │
│ Pause           │ P / Esc / On-screen Button │
│ Menu Navigation │ Mouse Click                │
└─────────────────┴────────────────────────────┘

  ---

📂 Project Structure


1 src/main/java/edu/bauet/java/cse/duckrun/
2 ├── core/         # Game engine, loop logic, and global constants.
3 ├── entities/     # Player, Enemy, Obstacle, and Item classes.
4 ├── scenes/       # Screen management (Menu, Game, Story, Game Over).
5 ├── ui/           # Custom UI components (HealthBar, Menus).
6 └── utils/        # Asset loading, collision detection, and timers.

  ---

🖼️ Screenshots

**Main Menu**
![Main Menu](src/main/resources/images/ui/main_menu.png)
​
**In-Game Action**
![Gameplay](src/main/resources/images/ui/main_menu.png)
​
 ---

🤝 Contributing


Contributions are what make the open-source community such an amazing place to learn, inspire, and create.


1. Fork the Project.
2. Create your Feature Branch (git checkout -b feature/AmazingFeature).
3. Commit your Changes (git commit -m 'Add some AmazingFeature').
4. Push to the Branch (git push origin feature/AmazingFeature).
5. Open a Pull Request.

  ---

📜 License

Distributed under the MIT License. See LICENSE for more information.

  ---

✉️ Contact


Project Link: https://github.com/your-username/DuckDash (https://github.com/your-username/DuckDash)


Developed with ❤️ by the Duck Dash Team.
