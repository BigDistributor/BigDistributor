### Headless Functions

##### 1- GenerateMetaBlocksFile.java
Divide the data in blocks and Generate a metadata json file with all the needed informations 
    - Params :
```sh
  -d, --data          Path of the Input Data
  -o, --overlap        Size of overlap between blocks
  -path             Path of the expected output file
  -s, --blockSize    Size of the expected blocks en pixels
```

Usage: 
```sh
GenerateMetaBlocksFile -d=<DATAPATH> -o=<OVERLAP> -path=<OUTPUT_PATH> -s=<BLOCK_SIZE>
```
- Example:
```sh
$ java GenerateMetaBlocksFile -d /Users/Marwan/Desktop/dataset.xml -s 200 -o 20 -path /Users/Marwan/Desktop/
```


##### 2- BlockExtractorUsingMetaBlocks
This generator take as parameter the data, the metablocks and the id of the block needed and the generate a block as tif file
    - Params :
```sh
  -d, --data        Path of the Input Data
  -id               Path of the MetaData file
  -m, --meta        Path of the MetaData file
  -path             Path of the output file 
```

Usage: 
```sh
BlockExtractorUsingMetaBlocks -d=<DATAPATH> -id=<BLOCK_ID> -m=<METABLOCKS_PATH> -path=<OUTPUT_PATH>
```
- Example:
```sh
$ java BlockExtractorUsingMetaBlocks -d /Users/Marwan/Desktop/dataset.xml -m /Users/Marwan/Desktop/METADATA.json -id 3 -path /Users/Marwan/Desktop/
```



