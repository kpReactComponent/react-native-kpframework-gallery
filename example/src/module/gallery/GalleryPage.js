/**
 * @author xukj
 * @date 2019/07/16
 * @class
 * @description 界面展示组件GalleryPage
 */
import React from 'react';
import PropTypes from 'prop-types';
import { StyleSheet, View } from 'react-native';
import { KPAndroidGalleryView } from 'react-native-kpframework-gallery';
import { Actions } from 'react-native-router-flux';

export default class GalleryPage extends React.PureComponent {
    static propTypes = {};

    static defaultProps = {};

    constructor(props) {
        super(props);
    }

    componentDidMount() {}

    render() {
        const largeImages = images
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images)
            .concat(images);
        return (
            <View style={styles.page}>
                <KPAndroidGalleryView
                    style={{ flex: 1 }}
                    options={{
                        images: largeImages,
                        debug: true,
                        mode: 'crop',
                        orientation: 'auto',
                        seek: true,
                    }}
                    onPageChanged={index => console.log('onPageChanged:' + index)}
                    onClosePress={() => {
                        console.log('关闭按钮点击');
                        Actions.pop();
                    }}
                    onClose={() => console.log('销毁')}
                />
            </View>
        );
    }
}

const styles = StyleSheet.create({
    page: {
        flex: 1,
    },
});

const images = [
    {
        source: require('../home/test00.png'),
        mode: 'crop',
    },
    {
        source: {
            uri:
                'http://m.qpic.cn/psu?/43967169/Y.YMon9Po5EyLcZVxakkQnZn0y.O5dEjvtvA0bKXv9A!/b/YfBXWBFokwAAYrBHfRI4VAAA&a=29&b=31&bo=ngKEAQAAAAABEC4!&rf=viewer_4',
        },
    },
    {
        source: {
            uri:
                'http://m.qpic.cn/psu?/43967169/.P4OPC7dpQi5WtD7AJjRMloPZJIM4w.5wSJ7wCiLFjM!/b/Yf.ZShHKVAAAYsfQfhK4VAAA&a=29&b=31&bo=AAKOAQAAAAABELo!&rf=viewer_4',
        },
        mode: 'custom',
        minScale: 0.5,
        maxScale: 2,
        debug: false,
    },
    {
        source: require('../home/test11.jpg'),
    },
];
