# Cinematograph

Cinematograph is a package for building a graph of actors and
films. The nodes are either actors or films and each edge in the graph
connects an actor with a film, indicating that said actor appeared in
said film.

There are undoubtedly much better ways to solve this problem, but I
wanted to test out the performance of shortcut with a large number of
nodes. This seems to do the trick as there are about 100k films and
75k actors in the data set.

Speaking of the dataset, the data comes from freebase's film database:
The original source of the data is:

http://www.freebase.com/view/film/film

However, gzipped copies of a snapshot of the data are in the .git repo.

For some examples of what cinematograph can do, check out
test/cinematograph_test.clj.

For example:

    cinematograph-test> (map :name (graph/find-node *actor-film-graph*
                     (get-actor-node "Aishwarya Rai")
                     (get-actor-node "Naomi Watts")))
    ("Naomi Watts" "Plots with a View" "Alfred Molina" "The Pink Panther 2" "Aishwarya Rai")

or:

    cinematograph-test> (map :name (graph/find-node *actor-film-graph*
                                (get-film-node "Dazed and Confused")
                                (get-film-node "Fast Times at Ridgemont High")))
    ("Fast Times at Ridgemont High" "Anthony Edwards" "Zodiac" "Adam Goldberg" "Dazed and Confused")

Feel free to contact me with complaints or suggestions at ch-lisp at bobobeach.com.

