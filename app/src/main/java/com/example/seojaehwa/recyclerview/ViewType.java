package com.example.seojaehwa.recyclerview;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef(value = {
        ViewType.PROGRESS,
        ViewType.ITEM_DEFAULT
})
@Retention(RetentionPolicy.SOURCE)
public @interface ViewType {
    int PROGRESS = -1;
    int ITEM_DEFAULT = 0;
}
