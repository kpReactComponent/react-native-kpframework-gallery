/**
 * @class
 * @author xukj
 * @date 2018/8/15
 * @description 界面路由
 */
import React from 'react';
import { Scene, Router, Stack } from 'react-native-router-flux';
import { HomePage } from '../module/home';
import { GalleryPage } from '../module/gallery';

export default class AppRouter extends React.Component {
    constructor(props) {
        super(props);
    }

    shouldComponentUpdate() {
        // react-native-router-flux 不支持重新注册
        return false;
    }

    render() {
        return (
            <Router>
                <Stack headerMode="screen" initial>
                    <Scene key="home" title="首页" component={HomePage} initial />
                    <Scene key="gallery" title="首页" component={GalleryPage} hideNavBar />
                </Stack>
            </Router>
        );
    }
}
