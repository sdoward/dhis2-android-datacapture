An example of the json returned by 'android/config' endpoint

```javascript
{
  "IDSR Weekly Diseaase Report(WDR)": {
    "id": "rq0LNr72Ndo",
    "shouldBeSquashed": true,
    "fieldGroups": [
      // a field group object contains the informations needed to group
      // fields into sections within the same form and create columns in the header

      // they structure of the columns must be a product,
      // first group is 1x2x2, second is 1x3

      {
        //the header should look like this
        //|  ABCDEFGHIJ  |   A   |   B   |
        //|              | 1 | 2 | 3 | 4 |
        "label": "ABCDEFGHIJ",

        // list of id of the fields that belong to this group
        "fields": [
          "qwHAEQqpzrC",
          "ZfVcoCBXP1m"
        ],
        "columns": [
          {
            "label": "A",
            "columns": [
              {
                "label": "1"
              },
              {
                "label": "2"
              }
            ]
          },
          {
            "label": "B",
            "columns": [
              {
                "label": "3"
              },
              {
                "label": "4"
              }
            ]
          }
        ]
      },
      {
        //the header should look like this
        //|  123456789  |  1  |  2  |  3  |
        "label": "123456789",
        "fields": [
          "qwHAEQqpzrC",
          "ZfVcoCBXP1m"
        ],
        "columns": [
          {
            "label": "1"
          },
          {
            "label": "2"
          },
          {
            "label": "3"
          }
        ]
      }
    ],
    "compulsoryDiseases": {
      "EIDSR-severe malnutrition": {
        "id": "FHfwXBYnhhl"
      }
      // ...
    },
    "diseaseConfigs": {
      "EIDSR-Diarrhoea": {
        "id": "PcFw3MoOC94",
        "disabledFields": [
          "OVER_FIVE_CASES",
          "OVER_FIVE_DEATHS"
        ],
        "isCritical": false,
        "isAdditionalDisease": false
      }
      // ...
    }
  }
}
```
