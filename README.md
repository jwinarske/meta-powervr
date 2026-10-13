# meta-powervr

Yocto/OpenEmbedded layer for the Imagination [PowerVR SDK](https://github.com/powervr-graphics/Native_SDK).

## Recipes

| Recipe | Version | Notes |
|---|---|---|
| `powervr-sdk` | 5.17 (R26.2) | framework + examples; GLES, Vulkan, Wayland via `PACKAGECONFIG` |

The SDK's `.gitmodules` point at Imagination's private mirrors, so submodules are fetched from their public upstreams at the commits R26.2 records.

## Compatibility

`LAYERSERIES_COMPAT`: scarthgap, whinlatter, wrynose, blacksail.

## Dependencies

- `openembedded-core`

Optional:

- `meta-clang`: builds `powervr-sdk` with clang/libc++ (`dynamic-layers/clang-layer`)

## Usage

```
bitbake-layers add-layer meta-powervr
bitbake powervr-sdk
```

`PACKAGECONFIG` defaults follow `DISTRO_FEATURES` (`opengl` → `gles`, `vulkan`, `wayland`). Also available: `glsc`, `opencl`.

## License

MIT, see [LICENSE](LICENSE). The PowerVR SDK itself is under its own license (`LICENSE.md` upstream).
