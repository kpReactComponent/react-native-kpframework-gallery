//
//  KPImageBrowser.h
//  KPGallery
//
//  Created by xukj on 2019/4/23.
//  Copyright © 2019 kpframework. All rights reserved.
//

#import "YBImageBrowser.h"
#import "KPImageBrowserDelegate.h"

NS_ASSUME_NONNULL_BEGIN

@interface KPImageBrowser : YBImageBrowser

@property (nonatomic, weak) _Nullable id<KPImageBrowserDelegate> kpDelegate;

@end

NS_ASSUME_NONNULL_END
