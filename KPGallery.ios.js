/**
 * @author xukj
 * @date 2019/04/10
 * @description Gallery.ios
 */
import React from 'react';
import { NativeEventEmitter, NativeModules } from 'react-native';
const resolveAssetSource = require('react-native/Libraries/Image/resolveAssetSource');

const KPPHOTO_GALLERY_EVENT_ONPAGECHANGED = 'KPPHOTO_GALLERY_EVENT_ONPAGECHANGED';
const KPPHOTO_GALLERY_EVENT_ONCLOSE = 'KPPHOTO_GALLERY_EVENT_ONCLOSE';

const KPNativeGallery = NativeModules.KPNativeGallery;
const NativeEventModule = new NativeEventEmitter(KPNativeGallery)

class GalleryControl {
    constructor() {
        this.onPageChangedListener = NativeEventModule.addListener(
            KPPHOTO_GALLERY_EVENT_ONPAGECHANGED,
            value => {
                if (this.onPageChanged) this.onPageChanged(value.index);
            }
        );

        this.onCloseListener = NativeEventModule.addListener(
            KPPHOTO_GALLERY_EVENT_ONCLOSE,
            () => {
                if (this.onClose) this.onClose();
                // 执行完成后清除
                this.removeOnPageChanged();
                this.removeOnClose();
            }
        );
    }

    addOnPageChanged = (onPageChanged) => {
        this.onPageChanged = onPageChanged;
    };

    removeOnPageChanged = () => {
        this.onPageChanged = null;
    }

    addOnClose = (onClose) => {
        this.onClose = onClose;
    };

    removeOnClose = () => {
        this.onClose = null;
    };
}

const control = new GalleryControl();

function showGallery(options = {}, onPageChanged = null, onClose = null) {
    // 需要把里面的images调整为android可识别的数据
    const values = options;
    let images = options.images;
    if (images) {
        images = images.map(image => {
            const source = resolveAssetSource(image.source);
            return {
                ...image,
                uri: source.uri,
            };
        });
        values.images = images;
    }

    control.addOnPageChanged(onPageChanged);
    control.addOnClose(onClose);
    
    KPNativeGallery.showGallery(values);
}

function getCacheSize() {
    return KPNativeGallery.getCacheSize({});
}

function clearCache() {
    return KPNativeGallery.clearCache({});
}

export default {
    showGallery,
    getCacheSize,
    clearCache,
};
