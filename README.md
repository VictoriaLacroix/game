# Game Engine

Run build.sh and everything should be good to go. Afterwards, run play.sh to run
the game.

## Directory Structure

bin/: Holds the binaries build by build.sh. Also contains a symlink to res/ such
that the program runs properly.

res/: All of the game's resources (maps, graphics, sounds, etc)

src/: Source code!

net/: A symlink for src, for building purposes. This is so the package names are
fulfilled without messing up the structure of the directory tree.

## Scripts

build.sh: builds the project into bin/

play.sh: plays the game!

## License

It's MIT-licensed. Go nuts.