# Soft Loader

Based on [Data Loader](https://github.com/Commander07/data-loader)

Allows datapacks in specified directories to automatically load as either **enabled** or **disabled** while remaining
optional.

## Configuration

The Soft Loader config will be created at `config/soft_loader.json` upon creating a world. You may also create this
file manually if needed.

### `enabled`: `string` <sup>optional</sup>

Specifies the directory for datapacks that should automatically load as **enabled**.

- Overridden by `initial-disabled-packs` for servers.

### `disabled`: `string` <sup>optional</sup>

Specifies the directory for datapacks that should automatically load as **disabled**.

- Overridden by `initial-enabled-packs` for servers.
- Overridden by `loadOrder` if a pack is listed there.

### `loadOrder`: `string[]` <sup>optional</sup>

Enforced only on world creation, specifies the load order for datapacks. Datapacks listed in `loadOrder` will
automatically be enabled regardless of its source directory.

- Can be used to enable and order built-in datapacks
- Built-in datapacks not included in `loadOrder` are placed at the bottom.
- Newly added datapacks, and other datapacks not included in `loadOrder`, are placed on top.
- Ignores `initial-disabled-packs` for servers.
