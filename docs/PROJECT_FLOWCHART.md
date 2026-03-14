# DuckRun (Duck Dash) - Project Flowchart

This document provides a formal, end-to-end flowchart of the DuckRun project: startup, scene navigation, the gameplay loop, entity spawning, collision outcomes, and the main code/package architecture.

Build/run context: Maven + JavaFX (`pom.xml` points to `edu.bauet.java.cse.duckrun.MainApp`).

## Runtime Workflow (Screens + Game Lifecycle)

```mermaid
flowchart TD
  %% =====================
  %% DuckRun runtime flow
  %% =====================

  A[User runs: mvn javafx:run] --> B[JavaFX Maven Plugin launches MainApp]
  B --> C[MainApp.start]
  C --> D[AssetLoader.preloadAssets - images cached]
  D --> E[StoryScene.createScene stage]

  E --> F{Intro video loads?\n/Story/opening.mp4}
  F -- yes --> G[MediaPlayer autoplay]
  G --> H{End of video\nor SPACE pressed}
  F -- no --> I[Fallback: skip intro]

  H --> J["StoryScene.navigateToMenu(stage)"]
  I --> J
  J --> K["MenuScene.createScene()"]
  K --> L{Menu action}

  L -- New Game --> M["Create Level1 groundY"]
  M --> N[Create GameScene Level]
  N --> O[GameScene.initialize backgroundPath]
  O --> P[Create scrolling background]
  O --> Q[Create Duck player]
  O --> R[Create HUD:\nHealthBar + SleepBar + Timer label]
  O --> S[Create pause system:\nPauseMenu + SettingsMenu + Pause button]
  O --> T[Setup keyboard controls\njump/crouch/pause]
  O --> U[Start loop:\nAnimationTimer + TimeUtil.start]

  %% Loop summary
  U --> V["Per-frame if not paused"]
  V --> W[Update background scroll]
  V --> X["Duck.update(deltaTime)\nanimation + jump physics + hitbox"]
  V --> Y["Spawn entities throttled\nvia Level.spawn*()"]
  V --> Z["Update/move entities\nand remove off-screen"]
  V --> AA[Collisions:\nDuck vs Enemy/Food/Obstacle]
  V --> AB{Game over condition?}
  AB -- HealthBar dead --> AC["GameScene.gameOver()\nstop loop"]
  AB -- SleepBar full --> AC["GameScene.gameOver()\nstop loop"]
  AB -- no --> V
  AC --> K

  %% Pause flow
  T --> PAUSE[ESC or pause button]
  PAUSE --> PB{Paused already?}
  PB -- no --> PC["Pause:\nTimeUtil.stop()\nShow PauseMenu\nHide pause button"]
  PB -- yes --> PD["Resume:\nReset frame clock\nTimeUtil.start()\nHide PauseMenu"]
  PC --> PE{Pause menu action}
  PE -- Resume --> PD
  PE -- Restart --> PR[Restart:\nreset duck + backgrounds\nclear entities\nreset bars/time]
  PR --> PD
  PE -- Settings --> PS[Show SettingsMenu overlay]
  PS --> PC
  PE -- Exit --> K

  %% Menu overlays (non-gameplay)
  L -- Levels --> ML[LevelMenu overlay]
  L -- High Score --> MH[HighScoreMenu overlay]
  L -- Settings --> MS[SettingsMenu overlay]
  L -- Exit --> MX["Stage.close()"]

  classDef io fill:#0b2a3a,stroke:#0b2a3a,color:#fff;
  classDef core fill:#163a24,stroke:#163a24,color:#fff;
  classDef ui fill:#2a1b3d,stroke:#2a1b3d,color:#fff;
  classDef decision fill:#3a2a0b,stroke:#3a2a0b,color:#fff;
  classDef loop fill:#1f2a44,stroke:#1f2a44,color:#fff;

  class A,MX io;
  class C,D,O,U core;
  class K,L,ML,MH,MS,S,PC,PD,PE,PS ui;
  class F,H,AB,PB decision;
  class V loop;
```

## Game Loop Detail (Per-Frame Update and Collision Outcomes)

```mermaid
flowchart TD
  %% ===========================
  %% GameScene per-frame details
  %% ===========================

  A["AnimationTimer.handle(now)"] --> B{First frame?}
  B -- yes --> C["Store lastFrameTime\nreturn"]
  B -- no --> D["Compute deltaTime: now-last/1e9"]
  D --> E{isPaused?}
  E -- yes --> Z[Skip updates]
  E -- no --> F["updateBackground(deltaTime)"]
  F --> G["duck.update(deltaTime)"]
  G --> H["spawnEntities - now - throttled + anti-repetition"]
  H --> I["updateEnemies - deltaTime"]
  I --> J["updateFoods - deltaTime"]
  J --> K["updateObstacles - deltaTime"]
  K --> L{SleepBar full?}
  L -- yes --> M["gameOver()"]
  L -- no --> N[Next frame]

  %% Enemies
  subgraph EN ["Enemy update + collision"]
    E1["Enemy.update - move left + animate + hitbox"] --> E2{Off-screen?}
    E2 -- yes --> E3["Remove from scene + list"]
    E2 -- no --> E4{Collides with Duck\nand not already collided?}
    E4 -- yes --> E5["duck.hit()\nHealthBar--\nSleepBar--\nTime +5s"]
    E5 --> E6{HealthBar dead?}
    E6 -- yes --> M
    E6 -- no --> E7[Continue]
    E4 -- no --> E7
  end

  %% Foods
  subgraph FO ["Food update + collision"]
    F1["Food.update - move left + hitbox"] --> F2{Off-screen?}
    F2 -- yes --> F3["Remove from scene + list"]
    F2 -- no --> F4{Collides with Duck?}
    F4 -- yes --> F5["Deactivate food\nHealthBar++ (if not full)\nSleepBar++\nduck.powerUp()\nTime +10s"]
    F4 -- no --> F6[Continue]
  end

  %% Obstacles
  subgraph OB ["Obstacle update + collision"]
    O1["Obstacle.update - move left + hitbox"] --> O2{Off-screen?}
    O2 -- yes --> O3["Remove from scene + list"]
    O2 -- no --> O4{Collides with Duck\nand not already collided?}
    O4 -- yes --> O5["duck.hit()\nHealthBar--\nSleepBar--\nTime +5s"]
    O5 --> O6{HealthBar dead?}
    O6 -- yes --> M
    O6 -- no --> O7[Continue]
    O4 -- no --> O7
  end

  %% Connect the main pipeline to concrete nodes inside subgraphs.
  %% (Linking to the subgraph id itself can render blank in Mermaid v10+.)
  I --> E1
  J --> F1
  K --> O1

  %% Color theme (so this diagram matches the styled Runtime Workflow)
  classDef core fill:#163a24,stroke:#34d399,color:#ffffff;
  classDef ui fill:#0b2a3a,stroke:#60a5fa,color:#ffffff;
  classDef decision fill:#3a2a0b,stroke:#fbbf24,color:#ffffff;
  classDef loop fill:#1f2a44,stroke:#93c5fd,color:#ffffff;
  classDef remove fill:#111827,stroke:#64748b,color:#e5e7eb;

  class A,D,F,G,H,I,J,K,N core;
  class C,Z,M,E3,F3,O3 remove;
  class B,E,L,E2,E4,E6,F2,F4,O2,O4,O6 decision;
  class E1,E5,E7,F1,F5,F6,O1,O5,O7 ui;
```

## Code/Package Architecture (How Modules Connect)

```mermaid
flowchart LR
  %% =====================
  %% Package architecture
  %% =====================

  subgraph APP ["edu.bauet.java.cse.duckrun"]
    MainApp["MainApp\n(JavaFX Application)"]
  end

  subgraph SCENES ["scenes"]
    StoryScene["StoryScene\n(intro video)"]
    MenuScene["MenuScene\n(main menu + overlays)"]
    GameScene["GameScene\n(gameplay + loop)"]
  end

  subgraph LEVELS ["levels"]
    Level["Level (abstract)\nbackground/speed/spawn*()"]
    Level1["Level1\n(Cat/Bread/Bottle)"]
  end

  subgraph ENT ["entities"]
    Duck["Duck (player)"]
    Enemy["Enemy (abstract)"]
    Food["Food (abstract)"]
    Obstacle["Obstacle (abstract)"]
    Cat["Cat : Enemy"]
    Eagle["Eagle : Enemy"]
    Bread["Bread : Food"]
    Worm["Worm : Food"]
    Bottle["Bottle : Obstacle"]
    Chair["Chair : Obstacle"]
    Tree["Tree : Obstacle"]
  end

  subgraph UI ["ui"]
    PauseMenu[PauseMenu]
    SettingsMenu[SettingsMenu]
    HealthBar[HealthBar]
    SleepBar[SleepBar]
    LevelMenu[LevelMenu]
    HighScoreMenu[HighScoreMenu]
  end

  subgraph UTIL ["utils"]
    AssetLoader["AssetLoader\n(cache + preload)"]
    CollisionUtil["CollisionUtil\n(Bounds.intersects)"]
    TimeUtil["TimeUtil\n(Timeline timer)"]
  end

  subgraph RES ["resources"]
    Images["images/\n(backgrounds, enemies, ui)"]
    Fonts["fonts/\n(PressStart2P, Jersey20)"]
    CSS["styles/\n(menu, pause, settings)"]
    Video["Story/opening.mp4"]
  end

  %% Entry + navigation
  MainApp --> AssetLoader
  MainApp --> StoryScene
  StoryScene --> MenuScene
  MenuScene --> GameScene
  MenuScene --> SettingsMenu
  MenuScene --> LevelMenu
  MenuScene --> HighScoreMenu

  %% Gameplay
  GameScene --> Duck
  GameScene --> Level
  Level1 --> Level
  GameScene --> Enemy
  GameScene --> Food
  GameScene --> Obstacle
  GameScene --> PauseMenu
  GameScene --> SettingsMenu
  GameScene --> HealthBar
  GameScene --> SleepBar
  GameScene --> CollisionUtil
  GameScene --> TimeUtil

  %% Entity types
  Level1 --> Cat
  Level1 --> Bread
  Level1 --> Bottle
  Cat --> Enemy
  Eagle --> Enemy
  Bread --> Food
  Worm --> Food
  Bottle --> Obstacle
  Chair --> Obstacle
  Tree --> Obstacle

  %% Asset usage
  Duck --> AssetLoader
  Enemy --> AssetLoader
  Food --> AssetLoader
  Obstacle --> AssetLoader
  PauseMenu --> Images
  SettingsMenu --> CSS
  MenuScene --> Images
  MenuScene --> Fonts
  StoryScene --> Video
  AssetLoader --> Images
  AssetLoader --> Video

  %% Color theme (explicit classes so this isn’t monochrome)
  classDef app fill:#163a24,stroke:#34d399,color:#ffffff;
  classDef scenes fill:#0b2a3a,stroke:#60a5fa,color:#ffffff;
  classDef levels fill:#1f2a44,stroke:#93c5fd,color:#ffffff;
  classDef entities fill:#0f172a,stroke:#a78bfa,color:#ffffff;
  classDef ui fill:#2a1b3d,stroke:#c084fc,color:#ffffff;
  classDef util fill:#3a2a0b,stroke:#fbbf24,color:#ffffff;
  classDef res fill:#2b2b2b,stroke:#fb923c,color:#ffffff;

  class MainApp app;
  class StoryScene,MenuScene,GameScene scenes;
  class Level,Level1 levels;
  class Duck,Enemy,Food,Obstacle,Cat,Eagle,Bread,Worm,Bottle,Chair,Tree entities;
  class PauseMenu,SettingsMenu,HealthBar,SleepBar,LevelMenu,HighScoreMenu ui;
  class AssetLoader,CollisionUtil,TimeUtil util;
  class Images,Fonts,CSS,Video res;
```

## Notes (What’s Implemented vs Placeholder)

- `Level1` is the active level wired from `MenuScene` and spawns `Cat`, `Bread`, `Bottle`.
- `Eagle`, `Worm`, `Chair`, `Tree` exist as entity classes but are not currently spawned by `Level1`.
- The following files are currently empty stubs (present but contain no code): `core/Constants.java`, `core/GameLoop.java`, `core/GameState.java`, `scenes/GameOverScene.java`, `ui/HUD.java`.
- Resource-path hygiene: `Chair` references `/images/obstacles/chair.png` and `Tree` references `/images/obstacles/tree.png`, but those files are not present under `src/main/resources` (current obstacle assets are `Chair_wood.png`, `chair_black.png`, etc.).
