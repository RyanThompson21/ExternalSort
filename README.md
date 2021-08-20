# ExternalSort
This program is meant to externally sort a binary file, using replacement selection, a minimum heap, and multi-way merge, of 8n blocks of data, where a block is 4096 bytes. The data is a series of 8 byte records. The first 4 bytes are the record ID and the second are the value. There are 8 blocks of working memory for the heap and 1 block each for input and output buffers. The first 8 blocks are read in and replacement selection is used to make the longest possible 'run'. The run is written into a seperate binary file one block at a time as the output buffer gets full. When the first run is complete more data is loaded and the process continues, until the end of file is reached. Next, the runs created are merged together, through the same process, and rewritten back into the original file. This happens until the file is one complete run.
