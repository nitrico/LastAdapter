[![Download](https://api.bintray.com/packages/moreno/maven/lastadapter/images/download.svg)](https://bintray.com/moreno/maven/lastadapter/_latestVersion)
[![License](https://img.shields.io/:License-Apache-orange.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# LastAdapter
Don't write a RecyclerView adapter again. Not even a ViewHolder!

## Usage

#### Java

```java
LastAdapter.with(Data.INSTANCE.getItems(), BR.item)
           .map(Header.class, R.layout.item_header)
           .map(Point.class, R.layout.item_point)
           .onBindListener(this)
           .into(list);
```

#### Kotlin

```kotlin
LastAdapter.with(Data.items, BR.item)
           .map<Header>(R.layout.item_header)
           .map<Point>(R.layout.item_point)
           .onBindListener(this)
           .into(list)
```


## Download

#### Gradle

```gradle
android {
    ...
    dataBinding { 
        enabled true 
    }
}

dependencies {
    compile 'com.github.nitrico.lastadapter:lastadapter:0.1.2'
}
```


## License
```txt
Copyright 2016 Miguel Ángel Moreno

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
