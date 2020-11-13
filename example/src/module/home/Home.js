/**
 * @class
 * @author xukj
 * @date 2018/8/15
 * @description 首页展示组件
 */
import React from 'react';
import PropTypes from 'prop-types';
import { View, Text, TouchableWithoutFeedback, StyleSheet } from 'react-native';

export default class Home extends React.PureComponent {
    static propTypes = {
        message: PropTypes.string,
        onPress: PropTypes.func,
        onSizePress: PropTypes.func,
        onClearPress: PropTypes.func,
        onViewModePress: PropTypes.func,
    };

    static defaultProps = {
        onPress: () => {},
    };

    constructor(props) {
        super(props);
        this.state = { visible: false, text: '' };
    }

    componentDidMount() {}

    render() {
        return (
            <View style={styles.page}>
                <Text style={styles.message}>{this.props.message}</Text>
                <View style={{ height: 50 }} />
                <TouchableWithoutFeedback onPress={this.props.onPress}>
                    <View>
                        <Text style={styles.button}>点击查看图片详情</Text>
                    </View>
                </TouchableWithoutFeedback>
                <View style={{ height: 20 }} />
                <TouchableWithoutFeedback onPress={this.props.onSizePress}>
                    <View>
                        <Text style={styles.button}>获取缓存大小</Text>
                    </View>
                </TouchableWithoutFeedback>
                <View style={{ height: 20 }} />
                <TouchableWithoutFeedback onPress={this.props.onClearPress}>
                    <View>
                        <Text style={styles.button}>清空缓存</Text>
                    </View>
                </TouchableWithoutFeedback>
                <TouchableWithoutFeedback onPress={this.props.onViewModePress}>
                    <View>
                        <Text style={styles.button}>view形式</Text>
                    </View>
                </TouchableWithoutFeedback>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    page: {
        flex: 1,
        alignItems: 'center',
        backgroundColor: 'white',
    },
    message: {
        marginTop: 20,
    },
    button: {
        marginTop: 20,
        borderColor: 'black',
        borderWidth: StyleSheet.hairlineWidth,
        borderRadius: 4,
        textAlign: 'center',
        padding: 8,
    },
});
