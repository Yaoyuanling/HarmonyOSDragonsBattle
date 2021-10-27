package com.example.snake;

import com.example.snake.slice.TimeOutAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;

public class TimeOutAbility extends Ability {
    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        super.setMainRoute(TimeOutAbilitySlice.class.getName());
    }
}
