/**
 * @author xukj
 * @date 2018/10/12
 * @description 配置第三方原生代码link
 */
const { execSync } = require('child_process');
const path = require('path');
const fs = require('fs');

/**
 * 配置iOS的cocoaPods
 */
function createCocoaPods() {
    console.info(
        '\x1B[33m开始创建Podfile, 如果失败请手动拷贝 script/data/Podfile 到 ios/ 文件夹下并修改target\x1B[0m'
    );

    const packageJSONPath = path.resolve('package.json');
    const packageJSON = JSON.parse(fs.readFileSync(packageJSONPath));
    const appName = packageJSON.name;

    console.info('当前项目名称: %s', appName);

    const templatePath = path.resolve('./script/data/Podfile');
    const destinationPath = path.resolve('./ios/Podfile');

    console.info('读取模板Podfile: %s', templatePath);
    const templateContent = fs.readFileSync(templatePath, 'utf8');
    console.info('生成Podfile: %s', destinationPath);
    fs.writeFileSync(
        destinationPath,
        templateContent.replace('<%= appName %>', appName),
        'utf8'
    );
    console.info('\x1B[32m创建Podfile成功\n\x1B[0m');
}

/**
 * 执行pod install
 */
function podInstall() {
    console.info(
        '\x1B[33m执行pod install, 需要首先安装cocoapods环境 https://cocoapods.org/\n如果失败请到/ios文件夹下手动执行 pod install\x1B[0m'
    );
    const podDir = path.resolve(process.cwd(), 'ios');
    execSync('pod install', { cwd: podDir, stdio: 'inherit' });
    console.info('\x1B[32m执行pod install成功\n\x1B[0m');
}

/**
 * 执行android 配置 升级 gradle
 */
function upgradeAndroidGradle() {
    console.info(
        '\x1B[33m开始升级android gradle, 如果失败请复制 /script/data/ 下的 \ngradle-wrapper.properties \nbuild.gradle \n 文件到对应的位置 \x1B[0m'
    );

    // 1. 升级 android/gradle/wrapper/gradle-wrapper.properties
    let templatePath = path.resolve('./script/data/gradle-wrapper.properties');
    let destinationPath = path.resolve('./android/gradle/wrapper/gradle-wrapper.properties');
    fs.copyFileSync(templatePath, destinationPath);
    // 2. 升级 android/build.gradle
    templatePath = path.resolve('./script/data/build.gradle');
    destinationPath = path.resolve('./android/build.gradle');
    fs.copyFileSync(templatePath, destinationPath);

    console.info('\x1B[32m升级android gradle成功\n\x1B[0m');
}

/**
 * link 原生代码
 */
function linkPackages() {
    console.info('\x1B[33m重新link第三方原生代码, 如果失败请重新执行\x1B[0m');
    const packages = ['rn-fetch-blob', 'react-native-vector-icons'];
    packages.forEach(package => {
        execSync(`react-native unlink ${package}`, { stdio: 'inherit' });
        execSync(`react-native link ${package}`, { stdio: 'inherit' });
    });
    console.info('\x1B[32m重新link第三方原生代码成功\n\x1B[0m');
}

function setup() {
    createCocoaPods();
    upgradeAndroidGradle();
    linkPackages();
    podInstall();
}

module.exports = {
    setup,
};
