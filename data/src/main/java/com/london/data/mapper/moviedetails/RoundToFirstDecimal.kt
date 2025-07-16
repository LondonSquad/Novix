package com.london.data.mapper.moviedetails

 fun Double.roundToFirstDecimal(): String {
    return "%.1f".format(this)
}
