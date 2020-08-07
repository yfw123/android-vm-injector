# Android Injector

## Usage

### .so注入 
```
$ artinjector -i <injecto_so> -p <package_name> --launch 
```
可选参数 [ --launch -ac <activity_name>]
* --launch: 将选择的应用置于等待调试器阶段,然后根据package_name启动应用
* -ac <activity_name>: 可自动启动选中的应用

现已支持root手机调试非debuggable的应用

### 检测app架构(32/64 bit)
```
$ artinjector -p <package_name> -a
```


## ErrorCodes

```
SOFILE_NOT_EXIST = 1 		    //.so文件不存在
CANT_FIND_DEVICE = 2;		    //无法找到设备
CANT_GET_CLIENT = 3;		    //选择的应用不是debuggable或者未启动
CANT_PUSH_FILE = 4;	        //无法将.so推入手机
CANT_ATTACH_APP = 5;		    //无法连接应用
BREAKPOINT_TIMEOUT = 6; 	    //断点超时
SOFILE_SHOULD_USE_32BIT = 7;    //应用为32位，使用了64位的.so
SOFILE_SHOULD_USE_64BIT = 8;    //应用为64位，使用了32位的.so
LOAD_SO_FAIL = 9;               //拉起so失败
```

## Tips
* 如果一直在等待断点阶段(wait breakpoints)，尝试切出应用再切回应用