# TestDBFlow

由于以前的app在使用过程中DB文件未加密。根据业务需求调整，现在的DB文件需要加密。

App 数据库使用框架以前使用的是DBFlow，因此做了个数据迁移测试app，对未加密的数据库加密，然后使用DbFlow打开继续使用.

这里使用WCDB sqlcipher 分别对数据库进行加密，并使用DbFlow 打开。
implementation "net.zetetic:android-database-sqlcipher:${sqlcipher_version}@aar" //加密必要
implementation 'com.tencent.wcdb:wcdb-android:1.0.8'

