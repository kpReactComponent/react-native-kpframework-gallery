/** @format */
import React from 'react';
import { AppRegistry } from 'react-native';
import App from './App';
import { name as appName } from './app.json';

const createApp = () => <App />;

AppRegistry.registerComponent(appName, () => createApp);
