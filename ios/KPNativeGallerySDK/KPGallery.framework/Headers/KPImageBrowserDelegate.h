//
//  KPImageBrowserDelegate.h
//  KPGallery
//
//  Created by xukj on 2019/4/23.
//  Copyright © 2019 kpframework. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN
@class KPImageBrowser;

@protocol KPImageBrowserDelegate <NSObject>

@optional;

- (void)imageBrowser:(KPImageBrowser *)browser pageIndexChanged:(NSUInteger)index;

- (void)imageBrowserClose;

@end

NS_ASSUME_NONNULL_END
