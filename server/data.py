import aioredis


DEFAULT_GROUP = 'blue:demo'

def _to_group(g):
    return g or DEFAULT_GROUP


def connect(url):
    """You must await this, returns a generator"""
    return aioredis.create_redis(url)


async def list(db, group=None):
    group = _to_group(group)
    data = await db.hgetall(group)
    return data


async def set(db, uuid, name, group=None):
    group = _to_group(group)
    data = await db.hset(group, uuid, name)
    return data


async def get(db, uuid, group=None):
    group = _to_group(group)
    data = await db.hget(group, uuid)
    return data


async def clear(db, group=None):
    group = _to_group(group)
    data = await db.delete(group)
    return data
