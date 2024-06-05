package dev.zenhao.melon.setting

import dev.zenhao.melon.module.AbstractModule

class StringSetting(name: String, contain: AbstractModule?, defaultValue: String) : Setting<String>(name, contain, defaultValue), SettingVisibility<StringSetting>
