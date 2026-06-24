# Native Android Pokédex Application

A native Android mobile application engineered in Kotlin. The system implements a dynamic client-side dataset management architecture to catalog characteristics, mapping smooth navigation flows across multiple interactive views.

## Key Features

* Native Android Architecture: Built leveraging native Android Activities (MainActivity, AddPokemonActivity, VisualizacionActivity) to manage clean Lifecycle states and intent-based data passing.
* Dynamic List Rendering: Implements a custom structural adapter (PokemonAdapter) linked to a performance-optimized RecyclerView for fluid dataset updates.
* Structured Domain Models: Features decoupled data serialization layers via explicit entity encapsulation (Pokemon.kt) to manage object structures.
* Scalable Foundation: Integrated with standard Gradle build configurations and standard build wrappers to ensure environmental portability.

## Tech Stack & Architecture Components

* Language: Kotlin (Native Mobile Development)
* Target SDK / Build Tool: Android Gradle Plugin (Kotlin DSL)
* UI Layouts: Android XML UI (Responsive component positioning)
* Component Patterns: Activity View Routing, RecyclerView Adapters, Domain Data Classes

## Structural Code Topography

The internal application core isolates presentation logic and component behaviors inside specific structural modules:
* `MainActivity.kt`: Serves as the primary operational dashboard orchestrating the main interactive canvas and rendering pipeline.
* `PokemonAdapter.kt`: Coordinates programmatic UI inflation and binds internal memory configurations directly into physical layout rows.
* `AddPokemonActivity.kt`: Governs validation scopes and manages logic constraints for structural input data insertion.
* `VisualizacionActivity.kt`: Drives data indexing layers to project isolated asset summaries based on state transfers.
