Change Log
==========


Version 1.2.1 *(2016-08-12)*
----------------------------

- Code cleanup


Version 1.2.0 *(2016-08-09)*
----------------------------

- **New:** Added `type` parameter to `onBind`, `onClick` and `onLongClick` methods in their respective interfaces. It is an int value that matches the layout resource id used for each item.
- **Fix:** `for` loop range in `WeakReferenceOnListChangedCallback.onItemRangeMoved`.
- `WeakReferenceOnListChangedCallback` class moved to a new file.
- Dependencies updated.


Version 1.1.0 *(2016-07-03)*
----------------------------

- **New:** Added lambda support in Kotlin for `onBind`, `onClick`, `onLongClick` and `layout`.


Version 1.0.0 *(2016-06-30)*
----------------------------

- Initial release.
