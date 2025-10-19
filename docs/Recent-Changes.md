# Recent Changes

## 2025-10-20 - Blood-Dawn
- Enforced non-null `User` authors for every `Post` instance and documented the invariants in code comments.
- Updated controller logic to always attach the active user or a persisted guest account before saving posts.
- Guarded repository writes against null authors and refreshed seeded demo users so UI cards always resolve an identity.
- Enhanced `PostCard` rendering to assume author presence and explain the rationale for the identity display logic.
