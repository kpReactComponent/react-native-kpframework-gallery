/**
 * @author xukj
 * @date 2018/9/12
 * @description 资源模块
 * 1.images 图片资源文件；
 * 2.styles 这里仅定义通用的样式文件，而非通用的样式请在各自的模块中定义；因为在快速迭代的App项目中，业务变更非常频繁，非通用的样式随着业务会发生频繁变动，如果放在asserts资源目录下，会影响开发效率；
 * 通用颜色文件；通用布局文件；通用字体文件；
 * 3.constant 需要用的各种通用常量、枚举定义；推荐项目中使用的各种type都定义到该处，提高可读性、易维护。
 * 4.other 其他资源相关的文件；
 */
import Colors from './Colors';

export { Colors };
