/**
 * @author xukj
 * @date 2018/8/15
 * @description Apps通用配置
 */
import React from 'react';
import AppRouter from './src/container/Router';

export default class App extends React.PureComponent {
    constructor(props) {
        super(props);
    }

    render() {
        return <AppRouter />;
    }
}
