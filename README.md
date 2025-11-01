<b>Fun</b><sub>ctional</sub> <b>Fluids</b> is an example/tutorial mod that demonstrates fluid registration and the
implementation of associated
functionality (cauldrons, buckets, etc.). It was created as a response to the state of fluid
documentation, the dysfunction of "official" documentation, and frequent complaints about the difficulty of
implementing fluids. In this mod, you will find full examples of common fluid implementations and copious
documentation in the form of docstrings and comments. It's recommended you pick an example fluid that does
what you want, and follow along while writing your own mod.
  <p>
  Fluid examples include:
  </p> 
<ul>
    <li>Cool Lava - Full recreation of vanilla lava EXCEPT for fire/ignition</li>
    <li>Thick Air - Fully transparent, non-flowing fluid. Balanced alternative to creative flight. Good for airlocks and underwater bubbles</li>
    <li>Configurable Fluid - A fluid with properties specified by the common config. Demonstrates datagenning dynamic fluid models, using common textures, and dynamically tinting items.</li>
</ul>
<p>
    <b>Fluid Registration Checklist</b> (WIP, some steps optional)
</p>
    <ul style="list-style: none;">
    <li><label><input type="checkbox">Create fluid type</label></li>
    <li><label><input type="checkbox">Create fluid</label></li>
    <li><label><input type="checkbox">Register fluid & type</label></li>
    <li><label><input type="checkbox">Register liquid block</label></li>
    <li><label><input type="checkbox">Register BucketItem</label></li>
    <li><label><input type="checkbox">Register cauldron block</label></li>
    <li><label><input type="checkbox">Add interactions for horizontally flowing fluid</label></li>
    <li><label><input type="checkbox">Finish fluid & type classes by adding references to registry objects</label></li>
    <li><label><input type="checkbox">Register client extensions</label></li>
    <li><label><input type="checkbox">Register tint and color handlers</label></li>
    <li><label><input type="checkbox">Datagen</label>
        <ul style="list-style: none;">
            <li><label><input type="checkbox">(Language) localization</label></li>
            <li><label><input type="checkbox">BlockStates</label></li>
            <li><label><input type="checkbox">ItemModels</label></li>
            <li><label><input type="checkbox">Recipes</label></li>
        </ul>
    </li>
    </ul>

  <p>
  Disclaimer: This mod is the product of my personal experience, made in isolation.
  It may have errors or inconsistencies. Feel free to make suggestions. Just remember - word of god doesn't mean
  anything when it comes to (Neo)Forge. Don't tell me I'm wrong until you've checked for yourself.
  </p>
