styles = {
    'Point': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'green',
            width: 10
        }),
        fill: new ol.style.Fill({
            color: 'rgba(20,165, 0)'
        })
    }),
    'LineString': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'red',
            width: 10
        })
    }),
    'MultiLineString': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'green',
            width: 1
        })
    }),
    'MultiPoint': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'yellow',
            width: 1
        }),
        fill: new ol.style.Fill({
            color: 'rgba(255, 255, 0, 0.1)'
        })
    }),
    'MultiPolygon': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'blue',
            lineDash: [4],
            width: 3
        }),
        fill: new ol.style.Fill({
            color: 'rgba(0, 0, 255,0.5)'
        })
    }),
    'Polygon': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'blue',
            lineDash: [4],
            width: 3
        }),
        fill: new ol.style.Fill({
            color: 'rgba(0, 0, 255,0.5)'
        })
    }),
    'GeometryCollection': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'magenta',
            width: 2
        }),
        fill: new ol.style.Fill({
            color: 'magenta'
        })
    }),
    'Circle': new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'red',
            width: 2
        }),
        fill: new ol.style.Fill({
            color: 'rgba(255,0,0,0.2)'
        })
    })
};

// 按照几何类型取得默认样式
var getStyle = function(feature) {
    return styles[feature.getGeometry().getType()];
};


function tranformFeatures(features,sourcePro,targetPro) {
    features.forEach(function (feature) {
        let geometry = feature.getGeometry();
        let proGeometry = geometry.transform(sourcePro,targetPro);
        feature.setGeometry(proGeometry)
    })
    return features;
}

function tranformFeature(feature,sourcePro,targetPro) {
    let geometry = feature.getGeometry();
    let proGeometry = geometry.transform(sourcePro,targetPro);
    feature.setGeometry(proGeometry)
    return feature;
}

function tranform(lonlat,soursePro,targetPro) {
    return ol.proj.transform(lonlat,soursePro,targetPro)
}

function deepCopy(obj) {
    return JSON.parse(JSON.stringify(obj))
}

