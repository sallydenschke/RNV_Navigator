# RNV_Navigator
This is a simple program to explore a GTFS-dataset from public transport association RNV (Rhein-Neckar-Verkehr).<br>
The program is designed to lead users through the process by printing help on the console. It's also designed to interact with the user via console input.<br>
The program is split in two main funcitonalities:<br>
 1. Statistics and
 2. RNVRouter.<br>

## Preparations
Before any functionality can be used, the data on which to perform the operations have to be imported. This is done with the tooling in the importData package. For this, the user has to specify the paths to comma-separated txt files containing the GTFS.data. The latter is included in this repository (2001v2). However, the code is designed to run with any RNV GTFS-dataset. They can be downloaded freely at https://opendata.rnv-online.de/dataset/gtfs-general-transit-feed-specification <br>
The data should contain one file containing the Stops, one file containing the Routes, one file containing the trips and one file contining the Stop times.<br>

## Statistics
In the Statistics-package, users can get some statistics on Stops and Routes of the rnv bus and tram network:<br>
 - The 5 longest and shortest Routes in meters
 - The 5 longest and shortest Routes in minutes
 - Which Stops are the 5 busiest Stops in this network, i.e. the Stops that are most frequented<br>

## RNVRouter
The RNVRouter is a very simple navigator for the rnv bus and tram network. Users can:<br>
 - Search for the next Stop closest to their coordinates
 - Find out which Routes are available at the next Stop closest to their coordinates
 - Search for interchanges that are reacheable from the next Stop closest to their coordinates without having to change lines
 - Search for the closest accessible interchange
 
## Limitations
Please note: The Routes only account for one direction each. In the GTFS-dataset, both directions of each Route are stored as one Route. <br>
Additionally, any calculation of distances is simplified. The data only gives clues as to where each Stop is. Thus we do know the vertices of each Route, but we have to assume the Route as straight lines connecting the points.