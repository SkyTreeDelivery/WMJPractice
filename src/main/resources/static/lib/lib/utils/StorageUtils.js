

const requireDataType = {
    allDataFromGeotools : 0,
    provinceData: 1
}


function prepareTableDataSource(dataType, options) {
    if(dataType === requireDataType.allDataFromGeotools){
        sessionStorage.setItem("requireDataType",dataType)
        sessionStorage.setItem("tablename",options.tablename)
    }else if(dataType === requireDataType.provinceData){
        sessionStorage.setItem("requireDataType",dataType)
    }
}

function getTableDataSourceType() {
    return sessionStorage.getItem("requireDataType")
}
