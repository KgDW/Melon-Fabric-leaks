package dev.zenhao.melon.setting

import dev.zenhao.melon.module.AbstractModule

class BooleanSetting(name: String, contain: AbstractModule?, defaultValue: Boolean) :
        Setting<Boolean>(name, contain, defaultValue), SettingVisibility<BooleanSetting>