/**
 * Created by yan on 2015/4/24.
 */

var KINDERGARENER=1;//幼儿园
var PRIMARY=2;//小学
var JUNIOR=4;//初中
var SENIOR=8;//高中
var UNIVERSITY=16;//大学

function isKinderGarener(type){
    return (type & KINDERGARENER) ==KINDERGARENER;
}
function isPrimary(type){
    return  (type & PRIMARY) ==PRIMARY
}
function isJunior(type){
    return  (type & JUNIOR) ==JUNIOR
}
function isSenior(type){
    return  (type & SENIOR) ==SENIOR
}
function isUniversity(type){
    return  (type & UNIVERSITY) ==UNIVERSITY
}
