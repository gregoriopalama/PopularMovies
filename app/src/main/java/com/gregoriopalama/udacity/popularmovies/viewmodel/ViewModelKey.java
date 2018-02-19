package com.gregoriopalama.udacity.popularmovies.viewmodel;

import android.arch.lifecycle.ViewModel;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import dagger.MapKey;

/**
 * Used with Dagger in order to inject ViewModels
 *
 * @see com.gregoriopalama.udacity.popularmovies.di.ViewModelModule
 * @author Gregorio Palamà
 */

@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@MapKey
public @interface ViewModelKey {
    Class<? extends ViewModel> value();
}
