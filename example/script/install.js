/**
 * @author xukj
 * @date 2018/10/12
 * @description
 */
const linkScripts = require('./setupLink');
const dependence = require('./setupDevDependencies');
const otherScripts = require('./setupScripts');
const configScripts = require('./setupConfigs');

function install() {
    try {
        dependence.setup();
        linkScripts.setup();
        otherScripts.setup();
        configScripts.setup();
        console.log('\n\x1B[32m🎉 Setup is all completed!\x1B[0m\n');
    } catch (error) {
		console.log(error);
        console.log('\n\x1B[31m💥 Ops! An error has occured!\x1B[0m\n');
    }
}

install();
