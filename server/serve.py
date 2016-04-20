from aiohttp import web
import argparse
import asyncio


import data


## web app
async def echo(request):
    path = request.match_info['path']
    if path in CHARMAP:
        raise web.HTTPFound('/' + CHARMAP[path])
    else:
        text = '<html><head><meta charset="UTF-8"></head><body><center><h1>{}</h1></center></body></html>'.format(path)
        return web.Response(body=text.encode('utf-8'))


class Group:
    def __init__(self, db_url):
        self.db_url = db_url.split(':')

    async def doList(self, request):
        db = await data.connect(self.db_url)
        resp = await data.list(db)
        db.close()

        resp = {k.decode(): v.decode() for (k, v) in resp.items()}

        return web.json_response(resp)

    async def doClear(self, request):
        db = await data.connect(self.db_url)
        resp = await data.clear(db)
        db.close()

        return web.json_response({'ok': True})


    async def doSet(self, request):
        uuid = request.match_info['uuid']
        post = await request.post()
        name = post.get('name')
        if not name:
            return web.json_response({'ok': False})

        db = await data.connect(self.db_url)
        resp = await data.set(db, uuid, name)
        db.close()

        return web.json_response({'ok': True})

    async def doGet(self, request):
        uuid = request.match_info['uuid']
        db = await data.connect(self.db_url)
        resp = await data.get(db, uuid)
        db.close()

        if not resp:
            return web.json_response({})
        else:
            return web.json_response({uuid: resp.decode()})

    async def __aenter__(self):
        return await data.connect(self.db_url)

    async def __aexit__(self, *args, **kwargs):
        await db.close()


async def init(loop, host, port, db_url):
    group = Group(db_url)

    app = web.Application(loop=loop)
    app.router.add_route('GET', '/', group.doList)
    app.router.add_route('DELETE', '/', group.doClear)
    app.router.add_route('GET', '/{uuid}', group.doGet)
    app.router.add_route('POST', '/{uuid}', group.doSet)
    app.router.add_route('PUT', '/{uuid}', group.doSet)

    srv = await loop.create_server(app.make_handler(), host, port)
    print("Server started at http://{}:{}".format(host, port))
    return srv


if __name__ == '__main__':
    parser = argparse.ArgumentParser(description="Run a REST-auth server to create couchdb users")
    parser.add_argument('--port', default='8000', help='port to bind to')
    parser.add_argument('--host', default='localhost', help='interface to server on')
    parser.add_argument('--db', default='localhost:6379', help='url for redis')
    args = parser.parse_args()

    loop = asyncio.get_event_loop()
    loop.run_until_complete(init(loop, args.host, args.port, args.db))
    try:
        loop.run_forever()
    except KeyboardInterrupt:
        pass
