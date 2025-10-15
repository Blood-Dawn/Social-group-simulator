# Campus Board Product Roadmap

## 🚦 Status Snapshot
- **Overall**: Backend enablement is behind schedule; UI is ready but waiting on controller and repository implementations.
- **Current Blockers**: Authentication flow cannot be wired end-to-end until the in-memory user repository and controller validation logic ship.
- **Next Decision Points**: Confirm data layer readiness before Sprint 6 begins so the Admin UI split can start on time.
- **Detailed Tasks**: All engineering subtasks remain in [TODO.md](../TODO.md); roadmap items link directly to the relevant sections.

## 📅 Milestone Plan

| Milestone | Owner | Target Sprint | Status | Dependencies |
| --- | --- | --- | --- | --- |
| Authentication Foundation | Alex Ramirez (Backend) | Sprint 5 (Nov 18–29, 2025) | ⚠️ At Risk | Data layer implementations, controller wiring |
| Multi-user UI & Admin Experience | Priya Desai (Frontend) | Sprint 6 (Dec 2–13, 2025) | 🔒 Blocked | Authentication Foundation |
| Real-time Sync & Notifications | Jordan Lee (Platform) | Sprint 7 (Jan 6–17, 2026) | ⏳ Not Started | Multi-user UI & Admin Experience, EventBus enhancements |

> **Dependency Highlight**: Authentication must finish before the Admin UI split begins to avoid rework in session handling.

---

### 🛡️ Authentication Foundation
- **Goal**: Deliver secure login, user session validation, and backend hooks required by the UI.
- **Dependencies**: Repository implementations and controller validation logic must be finalized before UI wiring.
- **Key Tasks**:
  - [Complete InMemoryUserRepository.java](../TODO.md#data-layer-repository-implementations)
  - [Implement Controller.java authentication methods](../TODO.md#controller-layer-business-logic)
  - [Add missing fields to Post.java for author attribution](../TODO.md#data-layer-repository-implementations)
  - [Create LoginDialog.java for credential capture](../TODO.md#authentication-system)

### 👥 Multi-user UI & Admin Experience
- **Goal**: Enable differentiated experiences for students, staff, and administrators, including moderated content workflows.
- **Dependencies**: Requires completed authentication stack and consolidated event publishing from the controller.
- **Key Tasks**:
  - [Complete Main.java wiring for dependency injection](../TODO.md#main-application-wiring)
  - [Expand filter strategies for richer moderation tools](../TODO.md#search-filter-enhancements)
  - [Introduce PostValidator.java for role-specific validation](../TODO.md#validation-error-handling)
  - [Finalize command implementations for undo/redo support](../TODO.md#command-pattern-for-undoredo)

### 🔄 Real-time Sync & Notifications
- **Goal**: Keep clients updated via EventBus-driven real-time updates and notification hooks.
- **Dependencies**: Requires controller command pipeline and validated Post model updates from earlier milestones.
- **Key Tasks**:
  - [Implement SearchService.java to support live filtering](../TODO.md#search-filter-enhancements)
  - [Enhance EventBus integrations within view components](../TODO.md#view-layer-ui-implementation---completed-by-deo-10142025)
  - [Plan Event Calendar integration for campus events](../TODO.md#campus-specific-features)
  - [Extend responsive design for cross-device support](../TODO.md#ui-polish)

---

## 🔍 Status & Blockers Detail
- **Authentication Foundation (Sprint 5)**: Implementation work is concentrated on repository validation and controller logic. Completion is gated by data consistency checks in `InMemoryUserRepository`. Once resolved, UI login flow can be integrated without risk.
- **Multi-user UI & Admin Experience (Sprint 6)**: Blocked until authentication completes. UI components built by Deo are staging-ready but cannot be merged into production because user role routing depends on backend hooks.
- **Real-time Sync & Notifications (Sprint 7)**: Not started; planning tasks continue while waiting for event publishing guarantees from earlier milestones. Early design work can begin once controller commands are stable.

---

## 🔗 Cross-References
- Engineering owners should update [TODO.md](../TODO.md) as tasks move to "done" so roadmap status lines stay accurate.
- Sprint planning notes will be captured in `docs/Project-Readiness-Summary.md` for audit readiness.
