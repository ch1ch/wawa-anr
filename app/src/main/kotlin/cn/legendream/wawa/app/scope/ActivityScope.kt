package cn.legendream.wawa.app.scope

import dagger.releasablereferences.CanReleaseReferences
import javax.inject.Scope

/**
 * Author: ZhaoXiyuan
 * Date: 2017/10/18
 * E-Mail: zhaoxiyuan@kokozu.net
 */

@Scope
@Retention(AnnotationRetention.RUNTIME)
@CanReleaseReferences
annotation class ActivityScope