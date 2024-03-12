package ru.nsu.gunko.lab3.model;

import ru.nsu.gunko.lab3.view.SkeletonP;

import java.util.ArrayList;
import java.util.List;

public class SkeletonL implements Logic{
    private final SkeletonP skeletonP;
    List<Integer> params;

    public SkeletonL(SkeletonP newSkeletonP) {
        skeletonP = newSkeletonP;
        params = new ArrayList<>();
        params.add(0); params.add(0);
        params.add(0);
        //x, y, side//
    }


}
