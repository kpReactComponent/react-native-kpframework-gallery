/**
 * @author xukj
 * @date 2018/10/19
 * @description ios-xcode10-script xcode10兼容脚本
 */
const path = require('path');
const fs = require('fs');
const {execSync} = require('child_process');

function fix() {
    console.info('\x1B[33m处理xcode10版本的react-native脚手架兼容性问题\x1B[0m');
    const rnRoot = path.resolve(process.cwd(), 'node_modules/react-native');
    // 下载ios相关的的包
    execSync('./scripts/ios-install-third-party.sh', {cwd: rnRoot, stdio: 'inherit'});

    // 执行glog
    const glogName = ((path) => {
        const subPaths = fs.readdirSync(path);
        let returnName;
        for (let i = 0; i < subPaths.length; i++) {
            const name = subPaths[i];
            const info = fs.statSync(path + "/" + name);
            if (info.isDirectory() && name.indexOf("glog") == 0) {
                returnName = name;
                break;
            }
        }
        return returnName;
    })(path.resolve(rnRoot, 'third-party'));

    if (!glogName) {
        console.error('\n\x1B[31m没有找到相关的文件，请使用手动方法修复 xcode10 无法运行的错误\x1B[0m\n');
        console.error('\n\x1B[31m1. 在xcode10中选择 File -> Project/Workspace settings\x1B[0m\n');
        console.error('\n\x1B[31m2. 在打开的界面选择 Build System: dropdown -> change to Legacy Build system\x1B[0m\n');
        process.exit(1);
        return;
    }

    const glogPath = path.resolve(rnRoot, `third-party/${glogName}`);
    execSync('../../scripts/ios-configure-glog.sh ', {cwd: glogPath, stdio: 'inherit'});
    console.info('\x1B[32m执行完成\n\x1B[0m');
}

fix();