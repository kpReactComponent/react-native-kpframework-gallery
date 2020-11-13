//
//  KPNativeBrowserToolBar.h
//  KPNativeGallery
//
//  Created by xukj on 2019/4/23.
//  Copyright © 2019 kpframework. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YBImageBrowserToolBarProtocol.h"

NS_ASSUME_NONNULL_BEGIN

@interface KPNativeBrowserToolBar : UIView<YBImageBrowserToolBarProtocol>

@property (nonatomic, strong, readonly) CAGradientLayer *gradient;
@property (nonatomic, strong, readonly) UILabel *indexLabel;
@property (nonatomic, strong, readonly) UIButton *closeButton;

@end

NS_ASSUME_NONNULL_END
