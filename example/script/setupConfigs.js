/**
 * @author xukj
 * @date 2019/03/13
 * @description 生成配置文件
 * 因为react-native自带的模板生成器无法生成隐藏文件,这里通过脚本来生成相关的配置文件
 */
const path = require('path');
const fs = require('fs');

/**
 * 创建 .babelrc 文件
 */
function createBabelrc() {
    console.info(
        '\x1B[33m开始创建 .babelrc, 如果失败请手动拷贝 script/data/.babelrc 到 根目录\x1B[0m'
    );

    const templatePath = path.resolve('./script/data/.babelrc');
    const destinationPath = path.resolve('./.babelrc');

    fs.copyFileSync(templatePath, destinationPath);

    console.info('\x1B[32m创建 .babelrc 成功\n\x1B[0m');
}

/**
 * 创建 .prettierrc
 */
function createPrettier() {
    console.info(
        '\x1B[33m开始创建 .prettierrc, 如果失败请手动拷贝 script/data/.prettierrc 到 根目录\x1B[0m'
    );

    const templatePath = path.resolve('./script/data/.prettierrc');
    const destinationPath = path.resolve('./.prettierrc');

    fs.copyFileSync(templatePath, destinationPath);

    console.info('\x1B[32m创建 prettierrc 成功\n\x1B[0m');
}

function setup() {
    createBabelrc();
    createPrettier();
}

module.exports = {
    setup,
};
