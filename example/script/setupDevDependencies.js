/**
 * @author xukj
 * @date 2018/10/15
 * @description
 */
const execSync = require('child_process').execSync;
const fs = require('fs');
const path = require('path');

/**
 * Use Yarn if available, it's much faster than the npm client.
 * Return the version of yarn installed on the system, null if yarn is not available.
 */
function getYarnVersionIfAvailable() {
    let yarnVersion;
    try {
        // execSync returns a Buffer -> convert to string
        yarnVersion = (
            execSync('yarn --version', {
                stdio: [0, 'pipe', 'ignore'],
            }).toString() || ''
        ).trim();
    } catch (error) {
        return null;
    }
    return yarnVersion;
}

function installDevDependencies() {
    console.log('\x1B[33mAdding dev dependencies for the project...\x1B[32m');

    const devDependenciesJsonPath = path.resolve(
        './script/data/devDependencies.json'
    );
    const devDependencies = JSON.parse(
        fs.readFileSync(devDependenciesJsonPath)
    );

    for (const depName in devDependencies) {
        const depVersion = devDependencies[depName];
        const depToInstall = `${depName}@${depVersion}`;
        console.log(`Adding ${depToInstall}...`);
        if (getYarnVersionIfAvailable()) {
            execSync(`yarn add ${depToInstall} -D`, { stdio: 'inherit' });
        } else {
            execSync(`npm install ${depToInstall} --save-dev`, {
                stdio: 'inherit',
            });
        }
    }

    console.log('install devDependencies complete\x1B[0m');
}

function setup() {
    installDevDependencies();
}

module.exports = {
    setup,
};
