// IMyService.aidl
package cn.dj.remoteserviceapp;
import cn.dj.remoteserviceapp.Result;

// Declare any non-default types here with import statements

interface IMyService {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    Result result(String phoneNum,String balance,String password);
}