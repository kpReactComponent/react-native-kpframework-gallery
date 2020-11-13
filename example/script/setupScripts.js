/**
 * @author xukj
 * @date 2018/10/19
 * @description setupOthers 其他配置脚本
 */
const fs = require('fs');
const path = require('path');

function setupScripts() {
    console.info('\x1B[33m配置script到package.json\x1B[0m');

    const packageJSONPath = path.resolve('package.json');
    let packageJSON = JSON.parse(fs.readFileSync(packageJSONPath));
    if (!packageJSON.scripts) {
        packageJSON.scripts = {};
    }

    packageJSON.scripts.iosFix = 'node script/ios-xcode10-script';
    fs.writeFileSync(
        packageJSONPath,
        JSON.stringify(packageJSON, null, 2),
        'utf8'
    );

    console.info('\x1B[m32添加脚本成功，请到package.json中查看');
}

module.exports.setup = setupScripts;
