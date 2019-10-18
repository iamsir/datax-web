# datax-web
基于datax，quartz的web端工具

##### 说明
 1、全量抽取：添加任务时，同步时间点为空即可，例子：
 ```json
 {
   "job": {
     "setting": {
       "speed": {
         "channel": 3
       },
       "errorLimit": {
         "record": 0,
         "percentage": 0.02
       }
     },
     "content": [{
       "reader": {
         "name": "mysqlreader",
         "parameter": {
           "username": "root",
           "password": "******",
           "column": [
               "app_id", "app_name", "package_name", "version", "create_user", "create_time", "update_user", "update_time"
           ],
           "splitPk": "app_id",
           "connection": [{
             "table": ["app_list"],
             "jdbcUrl": ["jdbc:mysql://127.0.0.1:3306/test1"]
           }]
         }
       },
       "writer": {
         "name": "mysqlwriter",
         "parameter": {
           "writeMode": "replace",
           "username": "root",
           "password": "Aa123456",
           "column": [
             "app_id", "app_name", "package_name", "version", "create_user", "create_time", "update_user", "update_time"
           ],
           "session": [
             "set session sql_mode='ANSI'"
           ],
           "connection": [
             {
               "jdbcUrl": "jdbc:mysql://127.0.0.1:3306/test2",
               "table": [
                 "app_list"
               ]
             }
           ]
         }
       }
     }]
   }
 }

```
 2、增量抽取：添加任务时，同步时间点填写yyyyMMdd格式，则任务开始时，
 会抽取yyyyMMdd+1日的数据。如下reader的where所示：
```json
{
  "job": {
    "setting": {
      "speed": {
        "channel": 3
      },
      "errorLimit": {
        "record": 0,
        "percentage": 0.02
      }
    },
    "content": [{
      "reader": {
        "name": "mysqlreader",
        "parameter": {
          "username": "root",
          "password": "******",
          "column": ["id", "create_time", "update_time", "model", "package_name", "app_version", "points", "'sz' as area"],
          "where" : "update_time >= ${create_time} and update_time < ${end_time}", // create_time=同步时间点日期+1日, end_time=create_time+1日
          "splitPk": "id",
          "connection": [{
            "table": ["app_grade"],
            "jdbcUrl": ["jdbc:mysql://127.0.0.1:3306/test1"]
          }]
        }
      },
      "writer": {
        "name": "mysqlwriter",
        "parameter": {
          "writeMode": "replace",
          "username": "root",
          "password": "******",
          "column": [
            "id", "create_time", "update_time", "model", "package_name", "app_version", "points", "area"
          ],
          "session": [
            "set session sql_mode='ANSI'"
          ],
          "connection": [
            {
              "jdbcUrl": "jdbc:mysql://127.0.0.1:3306/test2",
              "table": [
                "app_grade"
              ]
            }
          ]
        }
      }
    }]
  }
}
``` 
 

