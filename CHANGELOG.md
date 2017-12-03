Change Log
==========

Version 2.3.0 *(2017-11-28)*
----------------------------
- **Fix**: [`IndexOutOfBoundExeption` when not using ObservableList](https://github.com/nitrico/LastAdapter/issues/28)
- Update target SDK to 27
- Update Kotlin to version 1.2.0
- Update support libraries to version 27.0.2
- Update Android build tools to version 27.0.1
- Update Android Gradle Plugin to version 3.0.1

Version 2.2.0 *(2017-04-01)*
----------------------------

**Breaking changes**
- **New:** Removed `with` constructor – Use default constructors instead.
- **New:** Added `onCreate` callback as it is a more convenient place to set the click listeners.
- **New:** Click listeners are not set in on `onCreate`.
- **New:** Added support for different variable names depending on the type, while still support the old "only one variable name"-style for all types.
- **New:** ViewHolder class renamed to Holder.
- Smaller size despite including new features!

Version 2.1.0 *(2017-03-26)*
----------------------------

**Breaking changes**
- **New:** The ViewHolder is now the only argument in the callbacks. It was added to add ItemTouchHelper support but since the binding, the position and the view are inside the ViewHolder, this is actually the only argument needed.
- In Kotlin, arguments for Handlers (item & position) need to be explicitly declared now.
- Updated dependencies: Kotlin 1.1.1, Support libraries 25.3.0, Gradle 3.3, Gradle plugin 2.3.0.

Version 1.2.4 *(2016-11-07)*
----------------------------

- **Fix**: [Issue with support library 25.0.0](https://github.com/nitrico/LastAdapter/issues/9).

Version 1.2.3 *(2016-09-23)*
----------------------------

- Updated to Kotlin 1.0.4


Version 1.2.2 *(2016-09-09)*
----------------------------

- **Fix:** [IllegalStateException: reference.get() must not be null](https://github.com/nitrico/LastAdapter/issues/5).
- Removed unneeded "generics".
- Dependencies updated (which increased min SDK version from 7 to 9).


Version 1.2.1 *(2016-08-12)*
----------------------------

- Code cleanup.


Version 1.2.0 *(2016-08-09)*
----------------------------

- **New:** Added `type` parameter to `onBind`, `onClick` and `onLongClick` methods in their respective interfaces. It is an int value that matches the layout resource id used for each item.
- **Fix:** `for` loop range in `ListReference.onItemRangeMoved`.
- `ListReference` class moved to a new file.
- Dependencies updated.


Version 1.1.0 *(2016-07-03)*
----------------------------

- **New:** Added lambda support in Kotlin for `onBind`, `onClick`, `onLongClick` and `layout`.


Version 1.0.0 *(2016-06-30)*
----------------------------

- Initial release.
