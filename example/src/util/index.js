/**
 * @author xukj
 * @date 2018/9/12
 * @description 工具类，通用的功能性部分代码设计；可重用，高度独立；这部分可用于扩充代码库；
 * 1.网络请求、数据库操作、文件操作等
 * 因为网络请求在整个app中通用，因此设计为一个工具类，便于统一封装和拦截；其他类似；
 * 2.对象扩展
 * 在一些特定的情况下，我们会对已有的对象进行功能上的扩展，供App全局使用；
 * 例如：展示时间统一格式 xxxx年xx月xx日，可以设计一个统一的Number对象的功能扩展；
 * 3.便捷方法
 * 需要在App内多次用到，但是又不与业务耦合的功能；
 * 例如：emoji字符编码转换
 */
import { FSLStorage } from 'react-native-kpframework';
import _ from 'lodash';

global.storage = FSLStorage;
global._ = _;
