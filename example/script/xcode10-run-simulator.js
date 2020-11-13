/**
 * @author xukj
 * @date 2018/10/19
 * @description 修复xcode10无法运行模拟器
 */
const path = require('path');
const fs = require('fs');

const rnPath = 'node_modules/react-native';
const runSimulatorFilePath = 'local-cli/runIOS/findMatchingSimulator.js';

function fix() {
    console.info('\x1B[33m处理xcode10版本无法通过 run-ios 运行模拟器的问题\x1B[0m');

    // 需要修改的脚本地址
    const targetPath = path.resolve(process.cwd(), rnPath + '/' + runSimulatorFilePath);
    try {
        let content = fs.readFileSync(targetPath).toString();
        const originValue = "if (!version.startsWith('iOS') && !version.startsWith('tvOS'))";
        const replaceValue = "if (version.indexOf('iOS') !== 0 && !version.includes('iOS'))";
        content = content.replace(originValue, replaceValue);
        fs.writeFileSync(targetPath, content);
    } catch (error) {
        console.error(`\n\x1B[31m${error.message}\x1B[0m\n`);
        console.error('\n\x1B[31m修复失败，请使用手动实现\x1B[0m\n');
    }

    console.info('\x1B[32m执行完成\n\x1B[0m');
}

fix();